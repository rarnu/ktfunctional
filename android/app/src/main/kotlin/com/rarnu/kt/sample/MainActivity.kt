package com.rarnu.kt.sample

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.rarnu.kt.android.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        initUI()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE), 0)
        } else {
            test()
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>?, grantResults: IntArray?) {
        val canTest = grantResults?.all { it == PackageManager.PERMISSION_GRANTED } ?: return
        if (canTest) {
            test()
        }
    }

    private fun test() {

        testFile()
        testCommand()

        val a = 100.dip2px()
        val b = 1.dip2px()

        testurl("https://www.baidu.com:1234/uri/suburi?p1=a&p2=b")
        testurl("https://www.baidu.com:1234/uri/suburi?p1=&p2=b")
        testurl("https://www.baidu.com/uri/suburi?p1=a&p2=b")
        testurl("https://www.baidu.com:1234/suburi")
        testurl("https://www.baidu.com")
        testurl("www.baidu.com:1234")
        testurl("www.baidu.com")
    }

    private fun testZip() {

    }

    private fun testUnzip() {
        "".toEditable()

    }

    private fun testHttpRequest() {
        httpAsync {
            url = ""
            getParam = ""
            onSuccess { _, _, _ ->
                runOnUiThread {

                }
            }
        }
    }

    private fun testDownload() {

    }

    private fun testCommand() {
        runCommand {
            commands.add("ls")
            commands.add("/sdcard/")
            result { output, error ->
                Log.e("COMMAND", "output => $output, error => $error")
            }
        }
    }

    private fun testPackageParser() {

    }

    private fun testFile() {
        fileIO {
            src = "/sdcard/a.txt"
            isDestText = true
            result { status, text, errMsg ->
                if (status) {
                    Log.e("FILEIO", "READ OK => $text")
                } else {
                    Log.e("FILEIO", "READ ERROR => $errMsg")
                }
            }
        }
        fileIO {
            src = "/sdcard/a.txt"
            dest = "/sdcard/b.txt"
            result { status, text, errMsg ->
                if (status) {
                    Log.e("FILEIO", "COPY OK")
                } else {
                    Log.e("FILEIO", "COPY FAIL => $errMsg")
                }
            }
        }
        val dis = ByteArrayInputStream(byteArrayOf(1,2,3,4,5,6,7,8,9,10))
        val dos = ByteArrayOutputStream()
        fileIO {
            src = dis
            dest = dos
            result { status, text, errMsg ->
                val s = dos.size()
                print(s)
            }
        }
    }

    private fun testurl(url: String) {
        val info = decodeUrl(url)
        Log.e("URL", "info => protocol: ${info.proto}, port: ${info.port}, host: ${info.host}, uri: ${info.uri}")
        for((k, v) in info.params) {
            Log.e("URL", "k: $k, v: $v")
        }
    }
}
