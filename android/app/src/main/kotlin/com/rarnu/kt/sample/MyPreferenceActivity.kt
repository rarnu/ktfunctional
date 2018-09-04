package com.rarnu.kt.sample

import android.os.Bundle
import android.preference.Preference
import android.util.Log
import android.view.MenuItem
import com.rarnu.kt.android.PreferenceActivity
import com.rarnu.kt.android.showActionBack

class MyPreferenceActivity: PreferenceActivity(), Preference.OnPreferenceClickListener {

    private val TAG = "KTFUNCTIONAL"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showActionBack()
        actionBar.title = "Preference"
    }

    override fun getPreferenceXml() = R.xml.preference_my

    override fun onPreparedPreference() {
        (1..6).forEach { pref("key$it").onPreferenceClickListener = this }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        Log.e(TAG, preference.key)
        return true
    }


}