package com.rarnu.kt.android

import android.app.ActionBar
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import android.app.Activity

fun Activity.showActionBack() {
    actionBar?.setDisplayOptions(0, ActionBar.DISPLAY_HOME_AS_UP)
    actionBar?.setDisplayHomeAsUpEnabled(true)
}

abstract class PreferenceActivity : Activity() {

    abstract fun getPreferenceXml(): Int
    abstract fun onPreparedPreference()
    private val frag = InnerFragment()

    fun pref(resId: Int) = frag.findPreference(getString(resId))
    fun pref(key: String) = frag.findPreference(key)
    fun manager() = frag.preferenceManager
    fun screen() = frag.preferenceScreen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lay = RelativeLayout(this)
        lay.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        val frame = FrameLayout(this)
        frame.id = View.generateViewId()
        frame.layoutParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT)
        lay.addView(frame)
        setContentView(lay)
        fragmentManager.beginTransaction().replace(frame.id, frag).commit()
    }

    class InnerFragment : android.preference.PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setHasOptionsMenu(true)
            addPreferencesFromResource((activity as PreferenceActivity).getPreferenceXml())
            (activity as PreferenceActivity).onPreparedPreference()
        }
    }

}