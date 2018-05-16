package com.rarnu.kt.android

import android.content.Context
import android.text.Editable
import android.view.View
import android.widget.Toast
import android.app.Activity

fun String.toEditable(): Editable = Editable.Factory().newEditable(this)

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun Activity.v(resId: Int) = findViewById(resId)

fun View.v(resId: Int) = findViewById(resId)
