package com.rarnu.kt.ktor

import java.io.File
import java.security.MessageDigest

fun String.toJsonEncoded() = this.replace("\\", "\\\\").replace("\n", "\\n").replace("\"", "\\\"")
fun String.proj() = substring(0, 1).toUpperCase() + substring(1)

fun String.appendPathPart(part: String): String {
    val count =
        (if (isNotEmpty() && this[length - 1] == '/') 1 else 0) + (if (part.isNotEmpty() && part[0] == '/') 1 else 0)
    return when (count) {
        2 -> this + part.removePrefix("/")
        1 -> this + part
        else -> StringBuilder(length + part.length + 1).apply { append(this@appendPathPart); append('/'); append(part) }.toString()
    }
}

fun String.extension(): String {
    val indexOfName = lastIndexOf('/').takeIf { it != -1 } ?: lastIndexOf('\\').takeIf { it != -1 } ?: 0
    val indexOfDot = indexOf('.', indexOfName)
    return if (indexOfDot >= 0) substring(indexOfDot) else ""
}

fun String.save(dest: File) = dest.writeText(this)

fun String.hash(alg: String): String =
    try {
        val instance = MessageDigest.getInstance(alg)
        val digest = instance.digest(this.toByteArray())
        val sb = StringBuffer()
        for (b in digest) {
            val i = b.toInt() and 0xff
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                hexString = "0$hexString"
            }
            sb.append(hexString)
        }
        sb.toString()
    } catch (e: Exception) {
        ""
    }

fun String.asFileWriteText(text: String): File? {
    val dir = this.substringBeforeLast(File.separator)
    val dfile = File(dir)
    if (!dfile.exists()) {
        dfile.mkdirs()
    }
    val ffile = File(this)
    ffile.writeText(text)
    return ffile
}

fun String.asFileReadText(): String? {
    val ffile = File(this)
    return if (ffile.exists()) {
        ffile.readText()
    } else {
        null
    }
}

fun String.asFileMkdirs() {
    val dir = File(this)
    if (!dir.exists()) {
        dir.mkdirs()
    }
}

fun String.asFile() = File(this)