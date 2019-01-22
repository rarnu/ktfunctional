package com.rarnu.kt.sample

import android.content.Context
import android.view.View
import com.rarnu.kt.android.BaseAdapter
import kotlinx.android.synthetic.main.item_list.view.*

class MyAdapter(ctx: Context, list: MutableList<String>) : BaseAdapter<String, MyAdapter.MyAdapterHolder>(ctx, list) {

    override fun fillHolder(baseVew: View, holder: MyAdapterHolder, item: String, position: Int) {
        holder.tvItem.text = item
    }

    override fun getAdapterLayout() = R.layout.item_list

    override fun getValueText(item: String) = item

    override fun newHolder(baseView: View) = MyAdapterHolder(baseView)

    inner class MyAdapterHolder(v: View) {
        internal val tvItem = v.tvItem
    }

}