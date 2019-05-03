package com.rarnu.kt.ktor

val isMac: Boolean get() = System.getProperty("os.name").contains("Mac")
val isWindows: Boolean get() = System.getProperty("os.name").contains("Windows")
val isUnix: Boolean get() = !isMac && !isWindows
