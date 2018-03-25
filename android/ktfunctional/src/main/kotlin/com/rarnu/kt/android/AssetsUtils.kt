package com.rarnu.kt.android

import android.content.Context
import org.apache.commons.io.IOUtils
import java.io.File
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.OutputStream
import kotlin.concurrent.thread

class Assets {
    var context: Context? = null
    var src: String? = null
    /**
     * dest can be path(String), file(File), stream(OutputStream)
     */
    var dest: Any? = null
    var isDestText = false
    var _result:(Boolean, String?, String?) -> Unit = { _, _, _ -> }
    fun result(r:(status: Boolean, text: String?, errMsg: String?) -> Unit) {
        _result = r
    }
}


fun assetsIOAsync(init: Assets.() -> Unit) = thread { assetsIO(init) }

fun assetsIO(init: Assets.() -> Unit) {
    val a = Assets()
    a.init()
    AssetsOperations.assetsIO(a.context, a.src, a.dest, a.isDestText, a._result)
}

private object AssetsOperations {

    fun assetsIO(context: Context?, src: String?, dest: Any?, isDestText: Boolean, callback:(Boolean, String?, String?) -> Unit) {

        val ins = context?.assets?.open(src)
        if (isDestText) {
            try {
                val text = IOUtils.toString(ins)
                callback(true, text, null)
            } catch (e: Exception) {
                callback(false, null, e.message)
            }
        } else {
            when(dest) {
                is String -> {
                    try {
                        val fos = FileOutputStream(dest)
                        IOUtils.copy(ins, fos)
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
                        IOUtils.copy(ins, fos)
                        fos.flush()
                        fos.close()
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
                is OutputStream -> {
                    try {
                        IOUtils.copy(ins, dest)
                        callback(true, null, null)
                    } catch (e: Exception) {
                        callback(false, null, e.message)
                    }
                }
            }
        }
    }

}