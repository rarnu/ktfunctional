package com.rarnu.kt.sample

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.ScrollView
import com.rarnu.kt.android.runCommand
import com.rarnu.kt.android.showActionBack
import kotlinx.android.synthetic.main.activity_command.*

class CommandActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_command)
        showActionBack()
        actionBar?.title = "Command"

        // command
        btnRun.setOnClickListener {
            val cmd = etCmd.text.toString()
            if (cmd.trim() == "") {
                return@setOnClickListener
            }
            runCommand {
                commands.add(cmd)
                progress { _, value ->
                    addConsoleLog(value)
                }
                result { output, error ->
                    addConsoleLog(output)
                    addConsoleLog(error)
                }
            }
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}