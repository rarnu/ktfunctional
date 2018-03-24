package com.rarnu.kt

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
    var getParam = ""
    var postParam: Map<String, String> = hashMapOf()
    var fileParam: Map<String, String> = hashMapOf()
    var cookie: CookieJar? = null

    internal var _success: (Int, String?, CookieJar?) -> Unit = { _, _, _ -> }
    internal var _fail: (Throwable?) -> Unit = {}

    fun onSuccess(onSuccess: (Int, String?, CookieJar?) -> Unit) {
        _success = onSuccess
    }

    fun onFail(onFail: (Throwable?) -> Unit) {
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

private object HttpOperations {
    fun buildRequest(util: HttpUtils): Request {
        val req: Request
        when (util.method) {
            HttpMethod.GET -> { req = Request.Builder().url("${util.url}?${util.getParam}").build() }
            HttpMethod.POST -> {
                var u = util.url
                if (util.getParam != "") { u += "?${util.getParam}" }
                val body: RequestBody = if (util.fileParam.isEmpty()) {
                    buildBody(util.postParam)
                } else {
                    buildPostFileParts(util.postParam, util.fileParam)
                }
                req = Request.Builder().url(u).post(body).build()
            }
            HttpMethod.PUT -> {
                val body = buildBody(util.postParam)
                req = Request.Builder().url("${util.url}?${util.getParam}").put(body).build()
            }
            HttpMethod.DELETE -> {
                val body = buildBody(util.postParam)
                req = Request.Builder().url("${util.url}?${util.getParam}").delete(body).build()
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
            val value = params[key]!!
            builder.addFormDataPart(key, value)
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

    fun buildBody(params: Map<String, String>): RequestBody {
        val builder = FormBody.Builder()
        val iter = params.keys.iterator()
        while (iter.hasNext()) {
            val key = iter.next()
            val value = params[key]!!
            builder.add(key, value)
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
