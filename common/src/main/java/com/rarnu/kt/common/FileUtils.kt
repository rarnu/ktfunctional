package com.rarnu.kt.common

import org.apache.commons.io.IOUtils
import java.io.*
import java.nio.charset.Charset
import kotlin.concurrent.thread

class FileIO {
    /**
     * src can be path(String), file(File), stream(InputStream)
     */
    var src: Any? = null
    var isSrcText = false
    /**
     * dest can be path(String), file(File), stream(OutputStream)
     */
    var dest: Any? = null
    var isDestText = false

    var _result: (Boolean, String?, String?) -> Unit = { _, _, _ -> }
    fun result(r: (status: Boolean, text: String?, errMsg: String?) -> Unit) {
        _result = r
    }

}

fun fileIOAsync(init: FileIO.() -> Unit) = thread { fileIO(init) }

fun fileIO(init: FileIO.() -> Unit) {
    val f = FileIO()
    f.init()
    FileOperations.fileIO(f.src, f.isSrcText, f.dest, f.isDestText, f._result)
}

fun fileDeleteAsync(init: FileIO.() -> Unit) = thread { fileDelete(init) }

fun fileDelete(init: FileIO.() -> Unit) {
    val f = FileIO()
    f.init()
    FileOperations.fileDelete(f.src, f._result)
}

fun fileCopyAsync(init: FileIO.() -> Unit) = thread { fileCopy(init) }

fun fileCopy(init: FileIO.() -> Unit) {
    val f = FileIO()
    f.init()
    FileOperations.fileCopy(f.src, f.dest, f._result)
}

fun fileReadText(filePath: String): String {
    var ret = ""
    fileIO {
        src = filePath
        isDestText = true
        result { status, text, _ ->
            if (status) {
                ret = text!!
            }
        }
    }
    return ret
}

fun fileReadText(file: File): String {
    var ret = ""
    fileIO {
        src = file
        isDestText = true
        result { status, text, _ ->
            if (status) {
                ret = text!!
            }
        }
    }
    return ret
}

fun fileWriteText(filePath: String, text: String): Boolean {
    var ret = false
    fileIO {
        src = text
        dest = filePath
        isSrcText = true
        result { status, _, _ ->
            ret = status
        }
    }
    return ret
}

fun fileWriteText(file: File, text: String): Boolean {
    var ret = false
    fileIO {
        src = text
        dest = file
        isSrcText = true
        result { status, _, _ ->
            ret = status
        }
    }
    return ret
}

private object FileOperations {

    private const val ERROR_SRC = "Src Type Error"
    private const val ERROR_DEST = "Dest Type Error"

    fun fileCopy(src: Any?, dest: Any?, callback: (Boolean, String?, String?) -> Unit) {
        val fSrc = when(src) {
            is String -> File(src)
            is File -> src
            else -> null
        }
        if (fSrc == null) {
            callback(false, null, ERROR_SRC)
            return
        }
        val fDest = when(dest) {
            is String -> File(dest)
            is File -> dest
            else -> null
        }
        if (fDest == null) {
            callback(false, null, ERROR_DEST)
            return
        }
        if (fSrc.isFile) {
            try {
                fSrc.copyTo(fDest, true)
                callback(true, null, null)
            } catch (e: Exception) {
                callback(false, null, e.message)
            }
        } else {
            try {
                fSrc.copyRecursively(fDest, true)
                callback(true, null, null)
            } catch (e: Exception) {
                callback(false, null, e.message)
            }
        }
    }

    fun fileDelete(src: Any?, callback: (Boolean, String?, String?) -> Unit) {
        val fDelete = when (src) {
            is String -> File(src)
            is File -> src
            else -> null
        }
        if (fDelete == null) {
            callback(false, null, ERROR_SRC)
            return
        }
        if (fDelete.isFile) {
            try {
                fDelete.delete()
                callback(true, null, null)
            } catch (e: Exception) {
                callback(false, null, e.message)
            }
        } else {
            try {
                fDelete.deleteRecursively()
                callback(true, null, null)
            } catch (e: Exception) {
                callback(false, null, e.message)

            }
        }
    }

    fun fileIO(src: Any?, isSrctext: Boolean, dest: Any?, isDestText: Boolean, callback: (Boolean, String?, String?) -> Unit) {
        if (isSrctext) {
            textIOAny(src as String?, dest, isDestText, callback)
        } else {
            when (src) {
                is String -> fileIOAny(File(src), dest, isDestText, callback)
                is File -> fileIOAny(src, dest, isDestText, callback)
                is InputStream -> streamIOAny(src, dest, isDestText, callback)
                else -> callback(false, null, ERROR_SRC)
            }
        }
    }

    private fun streamIOAny(src: InputStream, dest: Any?, isDestText: Boolean, callback: (Boolean, String?, String?) -> Unit) {
        if (isDestText) {
            try {
                val text = IOUtils.toString(src, Charset.defaultCharset())
                callback(true, text, null)
            } catch (e: Exception) {
                callback(false, null, e.message)
            }

        } else {
            when (dest) {
                is String -> {
                    try {
                        val fos = FileOutputStream(dest)
                        IOUtils.copy(src, fos)
                        fos.flush()
                        fos.close()
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
                is File -> {
                    try {
                        val fos = FileOutputStream(dest)
                        IOUtils.copy(src, fos)
                        fos.flush()
                        fos.close()
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }

                }
                is OutputStream -> {
                    try {
                        IOUtils.copy(src, dest)
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
                else -> callback(false, null, ERROR_DEST)
            }
        }
    }

    private fun fileIOAny(src: File, dest: Any?, isDestText: Boolean, callback: (Boolean, String?, String?) -> Unit) {
        if (isDestText) {
            try {
                val fr = FileReader(src)
                val text = fr.readText()
                fr.close()
                callback(true, text, null)
            } catch (e: Exception) {
                callback(false, null, e.message)
            }
        } else {
            when (dest) {
                is String -> {
                    try {
                        src.copyTo(File(dest), true)
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
                is File -> {
                    try {
                        src.copyTo(dest, true)
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
                is OutputStream -> {
                    try {
                        val fis = FileInputStream(src)
                        IOUtils.copy(fis, dest)
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
                else -> callback(false, null, ERROR_DEST)
            }
        }
    }

    private fun textIOAny(text: String?, dest: Any?, isDestText: Boolean, callback: (Boolean, String?, String?) -> Unit) {
        if (isDestText) {
            callback(true, text, null)
        } else {
            when (dest) {
                is String -> {
                    try {
                        val fw = FileWriter(dest, false)
                        fw.write(text)
                        fw.flush()
                        fw.close()
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
                is File -> {
                    try {
                        val fw = FileWriter(dest, false)
                        fw.write(text)
                        fw.flush()
                        fw.close()
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
                is OutputStream -> {
                    try {
                        val dos = DataOutputStream(dest)
                        dos.writeBytes(text)
                        dos.flush()
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
                else -> callback(false, null, ERROR_DEST)
            }
        }
    }

}