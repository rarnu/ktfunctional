package com.rarnu.kt.android

import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration

fun Int.dip2px(): Int = (this * UIUtils.dm.density + 0.5f).toInt()
fun Int.px2scaled(): Float = (this / UIUtils.dm.density)
fun Float.scaled2px(): Int = (this * UIUtils.dm.density).toInt()

internal object UIUtils {

    internal val dm = DisplayMetrics()

    val density = dm.density
    var width = dm.widthPixels
    var height = dm.heightPixels

    fun init(activity: Activity) {
        activity.windowManager.defaultDisplay.getMetrics(dm)
    }

    fun actionBarHeight(ctx: Context): Int {
        val a = ctx.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize))
        val ret = a.getDimensionPixelSize(0, -1)
        a.recycle()
        return ret
    }

    fun statusBarHeight(ctx: Context) = with(ctx.resources) {
        getDimensionPixelSize(getIdentifier("status_bar_height", "dimen", "android"))
    }

    fun navigationBarHeight(ctx: Context) = with(ctx.resources) {
        getDimensionPixelSize(getIdentifier("navigation_bar_height", "dimen", "android"))
    }

    fun hasNavigationBar(ctx: Context) = if (Build.MODEL.toLowerCase().contains("nexus")) {
        (!ViewConfiguration.get(ctx).hasPermanentMenuKey() && !KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK))
    } else false

}