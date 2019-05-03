@file:Suppress("BlockingMethodInNonBlockingContext", "unused")

package com.rarnu.kt.ktor

import io.ktor.application.ApplicationCall
import io.ktor.http.decodeURLPart
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.normalizeAndRelativize
import java.io.File
import java.nio.file.Paths
import java.util.jar.JarFile

fun ApplicationCall.config(cfg: String): String = application.config(cfg)

@UseExperimental(KtorExperimentalAPI::class)
fun ApplicationCall.resolveFileContent(
    path: String,
    resourcePackage: String? = null,
    classLoader: ClassLoader = application.environment.classLoader
): String? {
    val packagePath = (resourcePackage?.replace('.', '/') ?: "").appendPathPart(path)
    val normalizedPath = Paths.get(packagePath).normalizeAndRelativize()
    val normalizedResource = normalizedPath.toString().replace(File.separatorChar, '/')
    for (url in classLoader.getResources(normalizedResource).asSequence()) {
        when (url.protocol) {
            "file" -> {
                val file = File(url.path.decodeURLPart())
                return if (file.isFile) file.readText() else null
            }
            "jar" -> {
                return if (packagePath.endsWith("/")) {
                    null
                } else {
                    val jar = JarFile(findContainingJarFile(url.toString()))
                    val jarEntry = jar.getJarEntry(normalizedResource)!!
                    val size = jarEntry.size
                    val b = ByteArray(size.toInt())
                    jar.getInputStream(jarEntry).read(b)
                    String(b)
                }
            }
        }
    }
    return null
}

@UseExperimental(KtorExperimentalAPI::class)
fun ApplicationCall.resolveFileBytes(
    path: String,
    resourcePackage: String? = null,
    classLoader: ClassLoader = application.environment.classLoader
): ByteArray? {
    val packagePath = (resourcePackage?.replace('.', '/') ?: "").appendPathPart(path)
    val normalizedPath = Paths.get(packagePath).normalizeAndRelativize()
    val normalizedResource = normalizedPath.toString().replace(File.separatorChar, '/')
    for (url in classLoader.getResources(normalizedResource).asSequence()) {
        when (url.protocol) {
            "file" -> {
                val file = File(url.path.decodeURLPart())
                return if (file.isFile) file.readBytes() else null
            }
            "jar" -> {
                return if (packagePath.endsWith("/")) {
                    null
                } else {
                    val jar = JarFile(findContainingJarFile(url.toString()))
                    val jarEntry = jar.getJarEntry(normalizedResource)!!
                    val size = jarEntry.size
                    val b = ByteArray(size.toInt())
                    jar.getInputStream(jarEntry).read(b)
                    b
                }
            }
        }
    }
    return null
}

@UseExperimental(KtorExperimentalAPI::class)
suspend fun ApplicationCall.resolveFileSave(
    dest: File,
    path: String,
    resourcePackage: String? = null,
    classLoader: ClassLoader = application.environment.classLoader
): Boolean {
    var ret = false
    val packagePath = (resourcePackage?.replace('.', '/') ?: "").appendPathPart(path)
    val normalizedPath = Paths.get(packagePath).normalizeAndRelativize()
    val normalizedResource = normalizedPath.toString().replace(File.separatorChar, '/')
    for (url in classLoader.getResources(normalizedResource).asSequence()) {
        when (url.protocol) {
            "file" -> {
                val file = File(url.path.decodeURLPart())
                if (file.isFile) {
                    file.copyTo(dest)
                    ret = true
                }
            }
            "jar" -> {
                if (!packagePath.endsWith("/")) {
                    val jar = JarFile(findContainingJarFile(url.toString()))
                    val jarEntry = jar.getJarEntry(normalizedResource)!!
                    jar.getInputStream(jarEntry).use { input ->
                        dest.outputStream().use { output ->
                            ret = input.copyToSuspend(output) > 0
                        }
                    }
                }
            }
        }
    }
    return ret
}

@UseExperimental(KtorExperimentalAPI::class)
fun ApplicationCall.resolveFile(
    path: String,
    resourcePackage: String? = null,
    classLoader: ClassLoader = application.environment.classLoader
): File? {
    val packagePath = (resourcePackage?.replace('.', '/') ?: "").appendPathPart(path)
    val normalizedPath = Paths.get(packagePath).normalizeAndRelativize()
    val normalizedResource = normalizedPath.toString().replace(File.separatorChar, '/')
    for (url in classLoader.getResources(normalizedResource).asSequence()) {
        when (url.protocol) {
            "file" -> {
                val file = File(url.path.decodeURLPart())
                println("file => $file")
                return if (file.isFile) file else null
            }
        }
    }
    return null
}

fun ApplicationCall.resourcePath(resourcePackage: String? = null) = application.resourcePath(resourcePackage)