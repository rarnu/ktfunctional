package com.rarnu.kt.android

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration

/**
 * make a dip int to pixel int
 */
fun Int.dip2px(): Int = (this * UI.dm.density + 0.5f).toInt()

fun Int.px2scaled(): Float = (this / UI.dm.density)
fun Float.scaled2px(): Int = (this * UI.dm.density).toInt()

object UI {

    internal lateinit var dm: DisplayMetrics
    /**
     * screen density
     */
    var density = 0.0F
    /**
     * screen width
     */
    var width = 0
    /**
     * screen height
     */
    var height = 0

    /**
     * UI.init(context) must be called before using any function in it.
     */
    fun init(ctx: Context) {
        dm = ctx.resources.displayMetrics
        density = dm.density
        width = dm.widthPixels
        height = dm.heightPixels
    }

    /**
     * actionbar height
     */
    fun actionBarHeight(ctx: Context): Int = ctx.obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).getDimensionPixelSize(0, -1)

    /**
     * statusbar height
     */
    fun statusBarHeight(ctx: Context) = with(ctx.resources) {
        getDimensionPixelSize(getIdentifier("status_bar_height", "dimen", "android"))
    }

    /**
     * navigationbar height
     */
    fun navigationBarHeight(ctx: Context) = with(ctx.resources) {
        getDimensionPixelSize(getIdentifier("navigation_bar_height", "dimen", "android"))
    }

    /**
     * navigationbar exists?
     * @comment only work on nexus devices
     */
    fun hasNavigationBar(ctx: Context) = if (Build.MODEL.toLowerCase().contains("nexus")) {
        (!ViewConfiguration.get(ctx).hasPermanentMenuKey() && !KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK))
    } else false

}