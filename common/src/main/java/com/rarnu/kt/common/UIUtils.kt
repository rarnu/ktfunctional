package com.rarnu.kt.common

import java.awt.Toolkit

object UI {

    /**
     * screen resolution
     */
    val density: Int
        get() = Toolkit.getDefaultToolkit().screenResolution

    /**
     * screen width
     */
    val width: Int
        get() = Toolkit.getDefaultToolkit().screenSize.width

    /**
     * screen height
     */
    val height: Int
        get() = Toolkit.getDefaultToolkit().screenSize.height

}