package com.rarnu.kt.android

import android.app.Activity
import android.content.AsyncTaskLoader
import android.content.Context
import android.database.Cursor
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast

fun String.toEditable(): Editable = Editable.Factory().newEditable(this)

fun Context.toast(message: String) = Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

fun <T : View> Activity.v(resId: Int) = findViewById<T>(resId)

fun <T : View> View.v(resId: Int) = findViewById<T>(resId)

fun Context.resStr(resId: Int) = resources.getString(resId)

fun Context.resStr(resId: Int, vararg args: Any?) = resources.getString(resId, *args)

fun Context.resStrArray(resId: Int) = resources.getStringArray(resId)

fun Context.resColor(resId: Int) = resources.getColor(resId, theme)

fun Context.resDrawable(resId: Int) = resources.getDrawable(resId, theme)


/**
 * T: Base Type in Adapter
 * H: Holder Class
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseAdapter<T, H>(ctx: Context, protected var list: MutableList<T>) : BaseAdapter(), Filterable {

    protected val lock = Any()
    private var inflater: LayoutInflater = LayoutInflater.from(ctx)
    protected lateinit var listFull: MutableList<T>
    private var _filter = ArrayFilter()
    protected val context = ctx

    open fun setNewList(list: MutableList<T>) {
        this.listFull = list
        this.list = list
        notifyDataSetChanged()
    }

    open fun deleteItem(item: T) {
        list.remove(item)
        listFull.remove(item)
        notifyDataSetChanged()
    }

    open fun deleteItems(items: MutableList<T>) {
        for (i in items) {
            list.remove(i)
            listFull.remove(i)
        }
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): Any? = list[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = list.size

    abstract fun getValueText(item: T): String?

    abstract fun getAdapterLayout(): Int

    abstract fun newHolder(baseView: View): H

    abstract fun fillHolder(baseVew: View, holder: H, item: T, position: Int)

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var v = convertView
        if (v == null) {
            v = inflater.inflate(getAdapterLayout(), parent, false)
        }
        var holder = v!!.tag as H?
        if (holder == null) {
            holder = newHolder(v)
            v.tag = holder
        }
        val item = list[position]
        fillHolder(v, holder!!, item, position)
        return v
    }

    open fun filter(text: String?) {
        _filter.filter(text)
    }

    override fun getFilter() = _filter

    inner class ArrayFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults? {
            list = listFull
            val results = FilterResults()
            if (prefix == null || prefix.isEmpty()) {
                synchronized(lock) {
                    val l = list
                    results.values = l
                    results.count = l.size
                }
            } else {
                val prefixString = prefix.toString().toLowerCase()
                val values = list
                val count = values.size
                val newValues = arrayListOf<T>()

                for (i in 0 until count) {
                    val value = values[i]
                    val valueText = getValueText(value)
                    if (valueText?.indexOf(prefixString) != -1) {
                        newValues.add(value)
                    }
                }
                results.values = newValues
                results.count = newValues.size
            }

            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            if (results != null) {
                list = results.values as MutableList<T>
                if (results.count > 0) {
                    notifyDataSetChanged()
                } else {
                    notifyDataSetInvalidated()
                }
            }
        }

    }

}

abstract class BaseClassLoader<T>(context: Context) : AsyncTaskLoader<T>(context) {
    abstract override fun loadInBackground(): T?
    override fun onStartLoading() = forceLoad()
    override fun onStopLoading() {
        cancelLoad()
    }

    override fun onReset() = stopLoading()
}

abstract class BaseListLoader<T>(context: Context) : BaseClassLoader<MutableList<T>?>(context) {
    abstract override fun loadInBackground(): MutableList<T>?
}

abstract class BaseCursorLoader(context: Context) : BaseClassLoader<Cursor>(context) {
    abstract override fun loadInBackground(): Cursor?
}