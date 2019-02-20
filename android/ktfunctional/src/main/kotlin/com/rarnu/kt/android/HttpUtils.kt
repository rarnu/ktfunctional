@file:Suppress("Duplicates")

package com.rarnu.kt.android

import okhttp3.*
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

/**
 * Created by rarnu on 6/8/17.
 */

enum class HttpMethod { GET, POST, PUT, DELETE }

class HttpUtils {

    var url = ""
    var method = HttpMethod.GET
    var mimeType = "text/json"
    var data = ""
    var getParam = ""
    var postParam = mutableMapOf<String, String>()
    var fileParam = mutableMapOf<String, String>()
    var cookie: CookieJar? = null
    var headers = mutableMapOf<String, String>()

    internal var _success: (Int, String?, CookieJar?) -> Unit = { _, _, _ -> }
    internal var _fail: (Throwable?) -> Unit = {}

    fun onSuccess(onSuccess: (code: Int, text: String?, cookie: CookieJar?) -> Unit) {
        _success = onSuccess
    }

    fun onFail(onFail: (error: Throwable?) -> Unit) {
        _fail = onFail
    }
}

fun httpAsync(init: HttpUtils.() -> Unit) = thread { http(init) }

fun http(init: HttpUtils.() -> Unit): String? {
    val h = HttpUtils()
    h.init()
    val req = HttpOperations.buildRequest(h)
    return HttpOperations.executeForResult(req, h)
}

fun httpGet(getUrl: String) = http {
    method = HttpMethod.GET
    url = getUrl
}

fun httpPost(postUrl: String, params: Map<String, String>) = http {
    method = HttpMethod.POST
    postParam.putAll(params)
    url = postUrl
}

fun httpUploadFile(postUrl: String, params: Map<String, String>, files: Map<String, String>) = http {
    method = HttpMethod.POST
    postParam.putAll(params)
    fileParam.putAll(files)
    url = postUrl
}

fun simpleHttpGet(getUrl: String): String {
    var ret = ""
    http {
        url = getUrl
        onSuccess { _, text, _ -> ret = text ?: "" }
    }
    return ret
}

fun simpleHttpPost(postUrl: String, params: Map<String, String>): String {
    var ret = ""
    http {
        url = postUrl
        postParam.putAll(params)
        onSuccess { _, text, _ -> ret = text ?: "" }
    }
    return ret
}

fun simpleUploadFile(postUrl: String, params: Map<String, String>, files: Map<String, String>): String {
    var ret = ""
    http {
        url = postUrl
        postParam.putAll(params)
        fileParam.putAll(files)
        onSuccess { _, text, _ -> ret = text ?: "" }
    }
    return ret
}

private object HttpOperations {

    private fun Request.Builder.headers(map: Map<String, String>) = this.headers(Headers.of(map))

    fun buildRequest(util: HttpUtils): Request {
        val req: Request
        when (util.method) {
            HttpMethod.GET -> { req = Request.Builder().url("${util.url}?${util.getParam}").headers(util.headers).get().build() }
            HttpMethod.POST -> {
                var u = util.url
                if (util.getParam != "") { u += "?${util.getParam}" }
                val body = if (util.data != "") {
                    buildDataBody(util.mimeType, util.data)
                } else {
                    if (util.fileParam.isEmpty()) {
                        buildBody(util.postParam)
                    } else {
                        buildPostFileParts(util.postParam, util.fileParam)
                    }
                }
                req = Request.Builder().url(u).headers(util.headers).post(body).build()
            }
            HttpMethod.PUT -> {
                var u = util.url
                if (util.getParam != "") { u += "?${util.getParam}" }
                val body = if (util.data != "") {
                    buildDataBody(util.mimeType, util.data)
                } else {
                    if (util.fileParam.isEmpty()) {
                        buildBody(util.postParam)
                    } else {
                        buildPostFileParts(util.postParam, util.fileParam)
                    }
                }
                req = Request.Builder().url(u).headers(util.headers).put(body).build()
            }
            HttpMethod.DELETE -> {
                val body = buildBody(util.postParam)
                req = Request.Builder().url("${util.url}?${util.getParam}").headers(util.headers).delete(body).build()
            }
        }
        return req
    }

    fun buildPostFileParts(params: Map<String, String>, files: Map<String, String>): RequestBody {
        val builder = MultipartBody.Builder()
        builder.setType(MultipartBody.FORM)
        val iterParam = params.keys.iterator()
        while (iterParam.hasNext()) {
            val key = iterParam.next()
            val value = params[key]
            builder.addFormDataPart(key, value!!)
        }
        val iterFile = files.keys.iterator()
        while (iterFile.hasNext()) {
            val key = iterFile.next()
            val file = files[key]
            val f = File(file)
            val fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), f)
            builder.addFormDataPart(key, file!!.substring(file.lastIndexOf("/") + 1), fileBody)
        }
        return builder.build()
    }

    fun buildDataBody(type: String, data: String) = RequestBody.create(MediaType.parse(type), data)

    fun buildBody(params: Map<String, String>): RequestBody {
        val builder = FormBody.Builder()
        val iter = params.keys.iterator()
        while (iter.hasNext()) {
            val key = iter.next()
            val value = params[key]
            builder.add(key, value!!)
        }
        return builder.build()
    }

    fun executeForResult(req: Request, util: HttpUtils): String? {
        val builder = OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
        if (util.cookie != null) { builder.cookieJar(util.cookie!!) }
        val http = builder.build()
        val call = http.newCall(req)
        var ret: String? = ""
        try {
            val resp = call.execute()
            if (resp.isSuccessful) {
                ret = resp.body()?.string()
            }
            util._success(resp.code(), ret, http.cookieJar())
        } catch (e: Throwable) {
            util._fail(e)
        }
        return ret
    }

}
