package com.rarnu.kt.sample

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.AdapterView
import android.widget.ScrollView
import com.rarnu.kt.android.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File

class MainActivity : Activity(), AdapterView.OnItemClickListener {

    private val HTTPROOT = "http://10.211.55.24:12345/phproot"
    private val HTTPURL = "http://10.211.55.24:12345/phproot/sample.php"

    private val lstItem = arrayListOf(
            "Http (Get)",                   // DONE
            "Http (Post)",                  // DONE
            "Http (Upload File)",           // DONE
            "Download",                     // DONE
            "FileIO (Text -> File)",        // DONE
            "FileIO (Text -> Stream)",      // DONE
            "FileIO (File -> Stream)",      // DONE
            "FileIO (Stream -> Text)",      // DONE
            "FileIO (Stream -> File)",      // DONE
            "FileIO (File -> Text)",        // DONE
            "AssetsIO (File -> Text)",      // DONE
            "AssetsIO (File -> Stream)",    // DONE
            "AssetsIO (File -> File)",      // DONE
            "URL Decode",                   // DONE
            "Context",                      // DONE
            "System",                       // DONE
            "Zip (Zip)",                    // DONE
            "Zip (Unzip)",                  // DONE
            "Image (...)",                  // DONE
            "Extension (...)",              // DONE
            "Adapter (...)",                // DONE
            "Preference (...)",             // DONE
            "Command (...)",                // DONE
            "Regular Expression"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        initUI()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showActionBack()
        actionBar?.title = "Kt.Functional"

        val adapter = MyAdapter(this, lstItem)
        lvItems.adapter = adapter
        lvItems.onItemClickListener = this

        if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.READ_PHONE_STATE), 0)
        }
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        when(position) {
            0 -> sampleHttpGet()
            1 -> sampleHttpPost()
            2 -> sampleHttpUploadFile()
            3 -> sampleDownload()
            4 -> sampleFileIOTF()
            5 -> sampleFileIOTS()
            6 -> sampleFileIOFS()
            7 -> sampleFileIOST()
            8 -> sampleFileIOSF()
            9 -> sampleFileIOFT()
            10 -> sampleAssetsIOFT()
            11 -> sampleAssetsIOFS()
            12 -> sampleAssetsIOFF()
            13 -> sampleURLDecode()
            14 -> sampleContext()
            15 -> sampleSystem()
            16 -> sampleZipZip()
            17 -> sampleZipUnzip()
            18 -> jumpImage()
            19 -> jumpExtension()
            20 -> jumpAdapter()
            21 -> jumpPreference()
            22 -> jumpCommand()
            23 -> sampleRegEx()
        }
    }

    private fun addConsoleLog(txt: String) {
        runOnUiThread {
            Handler().post {
                tvConsole.append("$txt\n")
                sv.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
    }

    // ===========================================================
    // HTTP
    // ===========================================================
    private fun sampleHttpGet() {
        // sample http get
        httpAsync {
            url = "$HTTPURL?m=get&gp=rarnuget"
            onSuccess { code, text, _ ->
                addConsoleLog("code: $code")
                addConsoleLog("data: $text")
            }

            onFail {
                addConsoleLog("error: ${it?.message}")
            }
        }
    }

    private fun sampleHttpPost() {
        // sample http post
        httpAsync {
            url = HTTPURL
            method = HttpMethod.POST
            postParam = mapOf("m" to "post", "pp" to "rarnupost")
            onSuccess { code, text, _ ->
                addConsoleLog("code: $code")
                addConsoleLog("data: $text")
            }
            onFail {
                addConsoleLog("error: ${it?.message}")
            }
        }
    }

    private fun sampleHttpUploadFile() {
        // sample http upload file
        fileIO {
            src = "sample upload file"
            dest = File(filesDir, "upload.txt")
            isSrcText = true
        }

        httpAsync {
            url = HTTPURL
            method = HttpMethod.POST
            postParam = mapOf("m" to "file", "fp" to "rarnufile")
            fileParam = mapOf("file" to File(filesDir, "upload.txt").absolutePath)
            onSuccess { code, text, _ ->
                addConsoleLog("code: $code")
                addConsoleLog("data: $text")
            }
            onFail {
                addConsoleLog("error: ${it?.message}")
            }
        }
    }

    // ===========================================================
    // DOWNLOAD
    // ===========================================================
    private fun sampleDownload() {
        // sample download
        downloadAsync {
            url = "$HTTPROOT/sample.txt"
            localFile = File(filesDir, "server.txt").absolutePath
            progress { state, position, fileSize, error ->
                addConsoleLog("$state, $position, $fileSize, $error")
                if (state == DownloadState.WHAT_DOWNLOAD_FINISH) {
                    addConsoleLog(localFile)
                }
            }
        }
    }

    // ===========================================================
    // FILEIO
    // ===========================================================
    private fun sampleFileIOTF() {
        // sample fileio text -> file
        addConsoleLog(filesDir.absolutePath)
        fileIO {
            src = "sample text"
            dest = File(filesDir, "sample.txt")
            isSrcText = true
            result { status, _, _ ->
                addConsoleLog(if (status) "TRUE" else "FALSE")
            }
        }
    }

    private fun sampleFileIOTS() {
        // sample fileio text -> stream
        val data = ByteArrayOutputStream()
        fileIO {
            src = "sample text"
            dest = data
            isSrcText = true
            result { status, _, errMsg ->
                if (status) {
                    addConsoleLog(data.toString())
                } else {
                    addConsoleLog("$errMsg")
                }
            }
        }
    }

    private fun sampleFileIOFS() {
        // sample fileio file -> stream
        val data = ByteArrayOutputStream()
        fileIO {
            src = File(filesDir, "sample.txt")
            dest = data
            result { status, _, errMsg ->
                if (status) {
                    addConsoleLog(data.toString())
                } else {
                    addConsoleLog("$errMsg")
                }
            }
        }
    }

    private fun sampleFileIOST() {
        // sample fileio stream -> text
        val data = ByteArrayInputStream("sample text".toByteArray())
        fileIO {
            src = data
            isDestText = true
            result { status, text, errMsg ->
                if (status) {
                    addConsoleLog("$text")
                } else {
                    addConsoleLog("$errMsg")
                }
            }
        }
    }

    private fun sampleFileIOSF() {
        // sample fileio stream -> file
        val data = ByteArrayInputStream("sample text".toByteArray())
        fileIO {
            src = data
            dest = File(filesDir, "sample.txt")
            result { status, _, _ ->
                addConsoleLog(if (status) "TRUE" else "FALSE")
            }
        }
    }

    private fun sampleFileIOFT() {
        // sample fileio file -> text
        fileIO {
            src = File(filesDir, "sample.txt")
            isDestText = true
            result { status, text, errMsg ->
                if (status) {
                    addConsoleLog("$text")
                } else {
                    addConsoleLog("$errMsg")
                }
            }
        }
    }


    // ===========================================================
    // ASSETSIO
    // ===========================================================
    private fun sampleAssetsIOFT() {
        // sample assetsio file -> text
        assetsIO {
            src = "b.txt"
            isDestText = true
            result { status, text, errMsg ->
                if (status) {
                    addConsoleLog("$text")
                } else {
                    addConsoleLog("$errMsg")
                }
            }
        }
    }

    private fun sampleAssetsIOFS() {
        // sample assetsio file -> stream
        val data = ByteArrayOutputStream()
        assetsIO {
            src = "b.txt"
            dest = data
            result { status, _, errMsg ->
                if (status) {
                    addConsoleLog(data.toString())
                } else {
                    addConsoleLog("$errMsg")
                }
            }
        }
    }

    private fun sampleAssetsIOFF() {
        // sample assetsio file -> file
        assetsIO {
            src = "b.txt"
            dest = File(filesDir, "assets.txt")
            result { status, _, _ ->
                addConsoleLog(if (status) "TRUE" else "FALSE")
            }
        }
    }

    // ===========================================================
    // URL DECODE
    // ===========================================================
    private fun sampleURLDecode() {
        // sample url decode
        fun testurl(url: String) {
            val info = decodeUrl(url)
            addConsoleLog("info => protocol: ${info.proto}, port: ${info.port}, host: ${info.host}, uri: ${info.uri}")
            for ((k, v) in info.params) {
                addConsoleLog("k: $k, v: $v")
            }
        }
        testurl("https://www.baidu.com:1234/uri/suburi?p1=a&p2=b")
        testurl("https://www.baidu.com:1234/uri/suburi?p1=&p2=b")
        testurl("https://www.baidu.com/uri/suburi?p1=a&p2=b")
        testurl("https://www.baidu.com:1234/suburi")
        testurl("https://www.baidu.com")
        testurl("www.baidu.com:1234")
        testurl("www.baidu.com")
    }

    // ===========================================================
    // CONTEXT
    // ===========================================================
    private fun sampleContext() {
        // sample context
        addConsoleLog("version code: $appVersionCode")
        addConsoleLog("version name: $appVersionName")
        writeConfig("samplekey", "sample value")
        val cfg = readConfig("samplekey", "")
        addConsoleLog("config: $cfg")
    }

    // ===========================================================
    // SYSTEM
    // ===========================================================
    private fun sampleSystem() {
        // sample system
        addConsoleLog("root: ${System.rooted}")
        addConsoleLog("busybox: ${System.busyboxInstalled}")
    }

    // ===========================================================
    // ZIP
    // ===========================================================
    private fun sampleZipZip() {
        // sample zip zip
        val fmgr = File(filesDir, "zip")
        fmgr.mkdirs()
        addConsoleLog(fmgr.absolutePath)
        fileIO {
            src = "SampleA"
            dest = File(fmgr, "a.txt")
            isSrcText = true
        }
        fileIO {
            src = "SampleB"
            dest = File(fmgr, "b.txt")
            isSrcText = true
        }
        fileIO {
            src = "SampleC"
            dest = File(fmgr, "b.txt")
            isSrcText = true
        }
        zip {
            zipPath = File(filesDir, "sample.zip").absolutePath
            srcPath = fmgr.absolutePath
            success {
                addConsoleLog("SUCC")
            }
            error {
                addConsoleLog("error: $it")
            }
        }
    }

    private fun sampleZipUnzip() {
        // sample zip unzip
        addConsoleLog(filesDir.absolutePath)
        unzip {
            zipPath = File(filesDir, "sample.zip").absolutePath
            destPath = File(filesDir, "unzip").absolutePath
            success {
                addConsoleLog("SUCC")
            }
            error {
                addConsoleLog("error: $it")
            }
        }
    }

    // ===========================================================
    // JUMP
    // ===========================================================
    private fun jumpImage() {
        // jump image
        startActivity(Intent(this, ImageActivity::class.java))
    }

    private fun jumpExtension() {
        // jump extension
        startActivity(Intent(this, ExtensionActivity::class.java))
    }

    private fun jumpAdapter() {
        // jump adapter
        startActivity(Intent(this, AdapterActivity::class.java))
    }

    private fun jumpPreference() {
        // jump preference
        startActivity(Intent(this, MyPreferenceActivity::class.java))
    }

    private fun jumpCommand() {
        // jump command
        startActivity(Intent(this, CommandActivity::class.java))
    }

    // ===========================================================
    // REGULAR EXPRESSION
    // ===========================================================
    private fun sampleRegEx() {
        val b1 = RegExUtils.isStringReg("abcdefg_123456", 5)
        val b2 = RegExUtils.isStringReg("abc我们_123", 5)
        addConsoleLog("b1: $b1, b2: $b2")

        val e1 = RegExUtils.isEmail("rarnu1985@gmail.com")
        val e2 = RegExUtils.isEmail("abcdefg.com")
        addConsoleLog("e1: $e1, e2: $e2")
    }
}
