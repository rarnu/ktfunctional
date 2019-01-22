package com.rarnu.kt.sample

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ScrollView
import com.rarnu.kt.android.showActionBack
import kotlinx.android.synthetic.main.activity_adapter.*
import java.util.*

class AdapterActivity: Activity(), AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {


    private val list = arrayListOf<String>()
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter)
        showActionBack()
        actionBar?.title = "Adapter"

        // adapter
        adapter = MyAdapter(this, list)
        lvSample.adapter = adapter
        srlPulldown.setOnRefreshListener {
            Handler().postDelayed({
                list.add(Random().nextInt().toString())
                adapter.notifyDataSetChanged()
                srlPulldown.isRefreshing = false
            }, 500)
        }
        lvSample.onItemClickListener = this
        lvSample.onItemLongClickListener = this

        edtFilter.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter.filter(edtFilter.text.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

        })
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

    override fun onItemLongClick(adapter: AdapterView<*>?, view: View?, position: Int, id: Long): Boolean {
        addConsoleLog("long press: ${list[position]}")
        return true
    }

    override fun onItemClick(adapter: AdapterView<*>?, view: View?, position: Int, id: Long) {
        addConsoleLog("clicked: ${list[position]}")
    }
}