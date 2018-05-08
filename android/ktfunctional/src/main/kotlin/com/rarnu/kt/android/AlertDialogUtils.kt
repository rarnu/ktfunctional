package com.rarnu.kt.android

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

fun alert(ctx: Context, title: String, message: String, btn1: String, btn2: String, callback: (which: Int) -> Unit) = AlertDialog.Builder(ctx)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(btn1, { _, _ -> callback(0) })
        .setNegativeButton(btn2, { _, _ -> callback(1) })
        .show()


fun alert(ctx: Context, title: String, message: String, btn1: String, btn2: String, btn3: String, callback: (which: Int) -> Unit) = AlertDialog.Builder(ctx)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(btn1, { _, _ -> callback(0) })
        .setNegativeButton(btn2, { _, _ -> callback(1) })
        .setNeutralButton(btn3, { _, _ -> callback(2) })
        .show()


fun alert(ctx: Context, title: String, message: String, btn1: String, btn2: String, placeholder: String?, initText: String?, callback: (which: Int, text: String) -> Unit) {
    val etText = EditText(ctx)
    etText.hint = placeholder
    etText.text = initText?.toEditable()
    AlertDialog.Builder(ctx)
            .setTitle(title)
            .setMessage(message)
            .setView(null)
            .setPositiveButton(btn1, { _, _ -> callback(0, etText.text.toString()) })
            .setNegativeButton(btn2, { _, _ -> callback(1, etText.text.toString()) })
            .show()
}
