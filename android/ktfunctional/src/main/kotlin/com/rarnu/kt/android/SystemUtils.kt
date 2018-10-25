package com.rarnu.kt.android

import android.annotation.SuppressLint


object System {
    val rooted = CommandOperations.rooted
    val busyboxInstalled = CommandOperations.busyboxInstalled

    @SuppressLint("PrivateApi")
    fun disableAndroidPNotes() {
        try {
            val clz = Class.forName("android.content.pm.PackageParser\$Package")
            val dc = clz.getDeclaredConstructor(String::class.java)
            dc.isAccessible = true
        } catch (e: Throwable) {

        }
        try {
            val clz = Class.forName("android.app.ActivityThread")
            val dc = clz.getDeclaredMethod("currentActivityThread")
            dc.isAccessible = true
            val activityThread = dc.invoke(null)
            val mws = clz.getDeclaredField("mHiddenApiWarningShown")
            mws.isAccessible = true
            mws.setBoolean(activityThread, true)
        } catch (e: Throwable) {

        }
    }

}