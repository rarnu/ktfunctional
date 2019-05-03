package com.rarnu.kt.ktor

import io.ktor.http.decodeURLPart
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.security.MessageDigest

val File.md5Sha1: String get() = hash("MD5") + hash("SHA1")

@Suppress("BlockingMethodInNonBlockingContext")
suspend fun InputStream.copyToSuspend(
        out: OutputStream,
        bufferSize: Int = DEFAULT_BUFFER_SIZE,
        yieldSize: Int = 4 * 1024 * 1024,
        dispatcher: CoroutineDispatcher = Dispatchers.IO
) = withContext(dispatcher) {
    val buffer = ByteArray(bufferSize)
    var bytesCopied = 0L
    var bytesAfterYield = 0L
    while (true) {
        val bytes = read(buffer).takeIf { it >= 0 } ?: break
        out.write(buffer, 0, bytes)
        if (bytesAfterYield >= yieldSize) {
            yield()
            bytesAfterYield %= yieldSize
        }
        bytesCopied += bytes
        bytesAfterYield += bytes
    }
    return@withContext bytesCopied
}

@UseExperimental(KtorExperimentalAPI::class)
fun findContainingJarFile(url: String): File {
    if (url.startsWith("jar:file:")) {
        val jarPathSeparator = url.indexOf("!", startIndex = 9)
        require(jarPathSeparator != -1) { "Jar path requires !/ separator but it is: $url" }
        return File(url.substring(9, jarPathSeparator).decodeURLPart())
    }
    throw IllegalArgumentException("Only local jars are supported (jar:file:)")
}

fun ByteArray.save(dest: File) = dest.writeBytes(this)

fun File.hash(alg: String): String {
    if (!this.isFile) {
        return ""
    }
    val digest = MessageDigest.getInstance(alg)
    val ins = FileInputStream(this)
    val buffer = ByteArray(1024)
    while (true) {
        val len = ins.read(buffer, 0, 1024)
        if (len == -1) break
        digest.update(buffer, 0, len)
    }
    ins.close()
    val bigInt = BigInteger(1, digest.digest())
    return bigInt.toString(16)
}