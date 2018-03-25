package com.rarnu.kt.sample

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import com.rarnu.kt.android.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        UI.init(this)
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

    }

    private fun testZip() {

    }

    private fun testUnzip() {

    }

    private fun testHttpRequest() {

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
    }
}
