@file:Suppress("DEPRECATION")

package com.rarnu.kt.android

import android.app.Activity
import android.content.AsyncTaskLoader
import android.content.Context
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.BaseAdapter

fun String.toEditable(): Editable = Editable.Factory().newEditable(this)

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT, dark: Boolean = true) = ExtendedToast.makeText(this, message, duration, dark).show()

fun <T : View> Activity.v(resId: Int) = findViewById<T>(resId)

fun <T : View> View.v(resId: Int) = findViewById<T>(resId)

fun Context.resStr(resId: Int) = resources.getString(resId)

fun Context.resStr(resId: Int, vararg args: Any?) = resources.getString(resId, *args)

fun Context.resStrArray(resId: Int) = resources.getStringArray(resId)

fun Context.resColor(resId: Int) = if (Build.VERSION.SDK_INT >= 23) { resources.getColor(resId, theme) } else { resources.getColor(resId) }

fun Context.resDrawable(resId: Int) = if (Build.VERSION.SDK_INT >= 21) { resources.getDrawable(resId, theme) } else { resources.getDrawable(resId) }

fun Context.attrColor(resId: Int): ColorStateList? {
    val a = obtainStyledAttributes(intArrayOf(resId))
    val color = a.getColorStateList(a.getIndex(0))
    a.recycle()
    return color
}

fun runOnMainThread(runnable: () -> Unit) = Handler(Looper.getMainLooper()).post { runnable() }

/**
 * T: Base Type in Adapter
 * H: Holder Class
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseAdapter<T, H>(ctx: Context, protected var list: MutableList<T>) : BaseAdapter(), Filterable {

    protected val lock = Any()
    private var inflater: LayoutInflater = LayoutInflater.from(ctx)
    protected var listFull = list
    private var _filter = ArrayFilter()
    protected val context = ctx

    fun resStr(resId: Int): String? = context.resStr(resId)
    fun resStr(resId: Int, vararg args: Any?) = context.resStr(resId, args)
    fun resStrArray(resId: Int) = context.resStrArray(resId)
    fun resColor(resId: Int) = context.resColor(resId)
    fun resDrawable(resId: Int) = context.resDrawable(resId)

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
            if (results?.values != null) {
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
    abstract override fun loadInBackground(): T
    override fun onStartLoading() = forceLoad()
    override fun onStopLoading() {
        cancelLoad()
    }
    override fun onReset() = stopLoading()
}

abstract class BaseListLoader<T>(context: Context) : BaseClassLoader<List<T>>(context) {
    abstract override fun loadInBackground(): List<T>
}

abstract class BaseMutableListLoader<T>(context: Context) : BaseClassLoader<MutableList<T>>(context) {
    abstract override fun loadInBackground(): MutableList<T>
}

abstract class BaseCursorLoader(context: Context) : BaseClassLoader<Cursor>(context) {
    abstract override fun loadInBackground(): Cursor
}

private class ExtendedToast private constructor(context: Context, text: String, duration: Int, isDark: Boolean) {
    private var t: Toast
    init {
        val txt = TextView(context)
        val gd = GradientDrawable()
        if (isDark) {
            gd.setColor(Color.argb(0xb0, 0x20, 0x20, 0x20))
            txt.setTextColor(Color.WHITE)
        } else {
            gd.setColor(Color.argb(0xb0, 0xcc, 0xcc, 0xcc))
            txt.setTextColor(Color.BLACK)
        }
        gd.cornerRadius = 5.dip2px().toFloat()
        txt.background = gd
        txt.setPadding(8.dip2px(), 8.dip2px(), 8.dip2px(), 8.dip2px())
        txt.text = text
        t = Toast(context)
        t.duration = duration
        t.view = txt
    }

    companion object {
        fun makeText(context: Context, text: String, duration: Int, isDark: Boolean = true): ExtendedToast {
            return ExtendedToast(context, text, duration, isDark)
        }
    }
    fun show() = t.show()
}