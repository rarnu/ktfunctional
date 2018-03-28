package com.rarnu.kt.android

import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

/**
 * Created by rarnu on 3/27/16.
 */

enum class DownloadState { WHAT_DOWNLOAD_START, WHAT_DOWNLOAD_PROGRESS, WHAT_DOWNLOAD_FINISH }

class Download {
    var url = ""
    var localFile = ""
    internal var _progress: ((DownloadState, Int, Int, String?) -> Unit) = { _, _, _, _ -> }
    fun progress(p: (state: DownloadState, position: Int, fileSize: Int, error: String?) -> Unit) {
        _progress = p
    }
}

fun downloadAsync(init: Download.() -> Unit) = thread { download(init) }

fun download(init: Download.() -> Unit) {
    val d = Download()
    d.init()
    DownloadOperations.downloadFile(d.url, d.localFile, d._progress)
}

private object DownloadOperations {

    fun downloadFile(address: String, localFile: String, handle: ((DownloadState, Int, Int, String?) -> Unit)?) {
        val fTmp = File(localFile)
        if (fTmp.exists()) {
            fTmp.delete()
        }
        val strFolder = localFile.substringBeforeLast("/")
        val fFolder = File(strFolder)
        if (!fFolder.exists()) {
            fFolder.mkdirs()
        }
        var isDownloadNormal = true
        var filesize = 0
        var position = 0
        try {
            val url = URL(address)
            val con = url.openConnection() as HttpURLConnection
            con.connectTimeout = 5000
            con.readTimeout = 5000
            val ins = con.inputStream
            filesize = con.contentLength
            handle?.invoke(DownloadState.WHAT_DOWNLOAD_START, position, filesize, null)
            val fileOut = File("$localFile.tmp")
            if (fileOut.exists()) {
                fileOut.delete()
            }
            val out = FileOutputStream(fileOut)
            val buffer = ByteArray(1024)
            var count: Int
            while (true) {
                count = ins.read(buffer)
                if (count != -1) {
                    out.write(buffer, 0, count)
                    position += count
                    handle?.invoke(DownloadState.WHAT_DOWNLOAD_PROGRESS, position, filesize, null)
                } else {
                    break
                }
            }
            ins.close()
            out.close()
            fileOut.renameTo(fTmp)
            handle?.invoke(DownloadState.WHAT_DOWNLOAD_FINISH, 0, filesize, null)
        } catch (e: Exception) {
            isDownloadNormal = false
            handle?.invoke(DownloadState.WHAT_DOWNLOAD_FINISH, 0, filesize, e.message)
        }
        if (!isDownloadNormal) {
            fTmp.delete()
        }
    }

}



