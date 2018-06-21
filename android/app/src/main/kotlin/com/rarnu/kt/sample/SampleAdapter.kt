package com.rarnu.kt.sample

import android.content.Context
import android.view.View
import com.rarnu.kt.android.BaseAdapter

class SampleAdapter(context: Context, list: MutableList<String>) : BaseAdapter<String, SampleAdapter.SampleHolder>(context, list) {
    override fun getValueText(item: String) = ""

    override fun getAdapterLayout() = R.layout.activity_main

    override fun newHolder(baseView: View) = SampleHolder()

    override fun fillHolder(baseVew: View, holder: SampleHolder, item: String, position: Int) {

    }


    inner class SampleHolder

}