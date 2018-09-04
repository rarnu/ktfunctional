package com.rarnu.kt.sample

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.ScrollView
import com.rarnu.kt.android.*
import kotlinx.android.synthetic.main.activity_extension.*

class ExtensionActivity: Activity(), View.OnClickListener {

    private val TAG = "KTFUNCTIONAL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extension)
        showActionBack()
        actionBar.title = "Extension"

        // extension
        btn0.setOnClickListener(this)
        btn1.setOnClickListener(this)
        btn2.setOnClickListener(this)
        btn3.setOnClickListener(this)
        btn4.setOnClickListener(this)
        btn5.setOnClickListener(this)
        btn6.setOnClickListener(this)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addConsoleLog(txt: String) {
        runOnUiThread {
            Handler().post {
                tvConsole.append("$txt\n")
                sv.fullScroll(ScrollView.FOCUS_DOWN)
            }
        }
    }

    override fun onClick(v: View) {
        val t = v.tag.toString().toInt()
        when(t) {
            0 -> {
                addConsoleLog("dip: 8, px: ${8.dip2px()}")
                addConsoleLog("actionbar height: ${actionBarHeight()}")
                addConsoleLog("statusbar height: ${statusBarHeight()}")
                addConsoleLog("has navigationbar: ${hasNavigationBar()}, height: ${navigationBarHeight()}")
            }
            1 -> {
                addConsoleLog("str: ${resStr(R.string.app_name)}")
                addConsoleLog("color: ${resColor(android.R.color.black)}")
                addConsoleLog("drawable: ${resDrawable(android.R.drawable.ic_menu_save)}")
            }
            2 -> alert("title", "message", "ok") { addConsoleLog("ok clicked") }
            3 -> alert("title", "message", "ok", "cancel") { which -> addConsoleLog("click $which") }
            4 -> alert("title", "message", "btn1", "btn2", "btn3") { which -> addConsoleLog("click $which") }
            5 -> alert("title", "message", "ok", "cancel", "input text", "") { which, text -> addConsoleLog("click: $which, text: $text") }
            6 -> alert("title", "message", "ok", "cancel", "account", "password", "", "") { which, text1, text2 -> addConsoleLog("click: $which, text1: $text1, text2: $text2") }

        }
    }
}