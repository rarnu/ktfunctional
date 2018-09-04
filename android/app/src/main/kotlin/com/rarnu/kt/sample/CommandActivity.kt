package com.rarnu.kt.sample

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import com.rarnu.kt.android.showActionBack

class CommandActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_command)
        showActionBack()
        actionBar.title = "Command"

        // TODO: command

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}