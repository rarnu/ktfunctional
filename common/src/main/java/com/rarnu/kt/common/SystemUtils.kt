package com.rarnu.kt.common

import java.lang.System

object System {

    private val OS = System.getProperty("os.name").toLowerCase()

    val isLinux: Boolean
        get() = OS.indexOf("linux") >= 0

    val isMacOS: Boolean
        get() = OS.indexOf("mac") >= 0 && OS.indexOf("os") > 0

    val isWindows: Boolean
        get() = OS.indexOf("windows") >= 0
}