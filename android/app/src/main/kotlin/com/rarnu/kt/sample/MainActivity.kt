package com.rarnu.kt.sample

import android.os.Bundle
import android.preference.Preference
import android.util.Log
import com.rarnu.kt.android.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class MainActivity : PreferenceActivity(), Preference.OnPreferenceClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        initUI()
        super.onCreate(savedInstanceState)
        showActionBack()
    }

    override fun getPreferenceXml() = R.xml.settings

    override fun onPreparedPreference() {
        val p = pref("key1")
        p?.onPreferenceClickListener = this
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

    override fun onPreferenceClick(preference: Preference?): Boolean {
        Log.e("PREF_CLICK", preference?.key)
        return true
    }
}
