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

/**
 * initUI() must be called before using any function in it.
 */
fun Context.initUI() {
    UI.dm = resources.displayMetrics
    UI.density = UI.dm.density
    UI.width = UI.dm.widthPixels
    UI.height = UI.dm.heightPixels
}

/**
 * actionbar height
 */
fun Context.actionBarHeight(): Int = obtainStyledAttributes(intArrayOf(android.R.attr.actionBarSize)).getDimensionPixelSize(0, -1)

/**
 * statusbar height
 */
fun Context.statusBarHeight() = with(resources) {
    getDimensionPixelSize(getIdentifier("status_bar_height", "dimen", "android"))
}

/**
 * navigationbar height
 */
fun Context.navigationBarHeight() = with(resources) {
    getDimensionPixelSize(getIdentifier("navigation_bar_height", "dimen", "android"))
}

/**
 * navigationbar exists?
 * @comment only work on nexus devices
 */
fun Context.hasNavigationBar() = if (Build.MODEL.toLowerCase().contains("nexus")) {
    (!ViewConfiguration.get(this).hasPermanentMenuKey() && !KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK))
} else false


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

}