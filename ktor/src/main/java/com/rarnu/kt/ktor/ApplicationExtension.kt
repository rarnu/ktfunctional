package com.rarnu.kt.ktor

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.*
import io.ktor.http.decodeURLPart
import io.ktor.sessions.Sessions
import io.ktor.sessions.cookie
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.normalizeAndRelativize
import java.io.File
import java.nio.file.Paths
import java.sql.Connection

@UseExperimental(KtorExperimentalAPI::class)
fun Application.config(key: String) = environment.config.property(key).getString()
fun Application.ifcfg(condition: Boolean, key1: String, key2: String) = if (condition) config(key1) else config(key2)



@UseExperimental(KtorExperimentalAPI::class)
fun Application.resourcePath(resourcePackage: String? = null): File? {
    val packagePath = (resourcePackage?.replace('.', '/') ?: "")
    val normalizedPath = Paths.get(packagePath).normalizeAndRelativize()
    val normalizedResource = normalizedPath.toString().replace(File.separatorChar, '/')
    for (url in environment.classLoader.getResources(normalizedResource).asSequence()) {
        when (url.protocol) {
            "file" -> {
                val file = File(url.path.decodeURLPart())
                return if (file.isDirectory) file else null
            }
        }
    }
    return null
}

inline val Application.db: DB get() = DB(this)
inline val Application.conn: Connection get() = db.conn()

inline fun <reified T: Any> Application.installPlugin(useCompress: Boolean = false, sessionIdentifier: String = "Session", headers: Map<String, String>? = null, init:() -> Unit) {
    install(Sessions) { cookie<T>(sessionIdentifier) { cookie.extensions["SameSite"] = "lax" } }
    if (useCompress) {
        install(Compression) {
            gzip { priority = 1.0 }
            deflate {
                priority = 10.0
                minimumSize(1024)
            }
        }
    }
    install(DefaultHeaders) { headers?.forEach { t, u -> header(t, u) } }
    install(PartialContent) { maxRangeCount = 10 }
    init()
}