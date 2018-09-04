package com.rarnu.kt.sample

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
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

    override fun onClick(v: View) {
        val t = v.tag.toString().toInt()
        when(t) {
            0 -> {
                Log.e(TAG, "dip: 8, px: ${8.dip2px()}")
                Log.e(TAG, "actionbar height: ${actionBarHeight()}")
                Log.e(TAG, "statusbar height: ${statusBarHeight()}")
                Log.e(TAG, "has navigationbar: ${hasNavigationBar()}, height: ${navigationBarHeight()}")
            }
            1 -> {
                Log.e(TAG, "str: ${resStr(R.string.app_name)}")
                Log.e(TAG, "color: ${resColor(android.R.color.black)}")
                Log.e(TAG, "drawable: ${resDrawable(android.R.drawable.ic_menu_save)}")
            }
            2 -> alert("title", "message", "ok") { Log.e(TAG, "ok clicked") }
            3 -> alert("title", "message", "ok", "cancel") { which -> Log.e(TAG, "click $which") }
            4 -> alert("title", "message", "btn1", "btn2", "btn3") { which -> Log.e(TAG, "click $which") }
            5 -> alert("title", "message", "ok", "cancel", "input text", "") { which, text -> Log.e(TAG, "click: $which, text: $text") }
            6 -> alert("title", "message", "ok", "cancel", "account", "password", "", "") { which, text1, text2 -> Log.e(TAG, "click: $which, text1: $text1, text2: $text2") }

        }
    }
}