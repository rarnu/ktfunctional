package com.rarnu.kt.android

class UrlInfo {
    var host = ""
    var port = 80
    var proto = "http"
    var uri = ""
    var params = mutableMapOf<String, String>()
}

fun decodeUrl(url: String): UrlInfo {
    val info = UrlInfo()

    var innerUrl = url
    if (innerUrl.contains("://")) {
        // find protocol
        val idx = innerUrl.indexOf(":")
        val p = innerUrl.substring(0, idx)
        info.proto = p
        if (p == "http") {
            info.port = 80
        } else if (p == "https") {
            info.port = 443
        }
        innerUrl = innerUrl.substring(idx + 3)

    }

    if (innerUrl.contains("/")) {
        // find uri or params
        val idx = innerUrl.indexOf("/")
        var sub = innerUrl.substring(idx + 1)
        if (sub.contains("?")) {
            // find params
            val idxParam = sub.indexOf("?")
            val paramStr = sub.substring(idxParam + 1)
            val ps = paramStr.split("&")
            for(s in ps) {
                val p = s.split("=")
                if (p.size == 1) {
                    info.params[p[0]] = ""
                } else {
                    info.params[p[0]] = p[1]
                }
            }
            sub = sub.substring(0, idxParam)
        }
        // treat uri
        info.uri = sub
        // slice to host and port
        innerUrl = innerUrl.substring(0, idx)

    }

    if (innerUrl.contains(":")) {
        // find port
        val idx = innerUrl.indexOf(":")
        val p = innerUrl.substring(idx + 1)
        info.port = p.toInt()
        innerUrl = innerUrl.substring(0, idx)
    }

    info.host = innerUrl

    return info
}