package com.rarnu.kt.sample

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import com.rarnu.kt.android.showActionBack
import kotlinx.android.synthetic.main.activity_adapter.*
import java.util.*

class AdapterActivity: Activity() {

    private val list = arrayListOf<String>()
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adapter)
        showActionBack()
        actionBar.title = "Adapter"

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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}