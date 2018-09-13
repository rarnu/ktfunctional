package com.rarnu.kt.common

import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ListCell
import javafx.scene.control.ListView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.Pane
import javafx.scene.paint.Color
import javafx.scene.text.Font


val BUTTON_TEXT_COLOR = Color.color(0.0, 0.478431, 1.0)!!

fun Button.ios(w: Int, h: Int) {
    background = Background(BackgroundFill(Color.TRANSPARENT, null, null))
    textFill = BUTTON_TEXT_COLOR
    font = Font.font(10.0)
    if (w != -1) prefWidth = w.toDouble()
    if (h != -1) prefHeight = h.toDouble()
}

fun Button.ios(w: Double, h: Double) {
    background = Background(BackgroundFill(Color.TRANSPARENT, null, null))
    textFill = BUTTON_TEXT_COLOR
    font = Font.font(10.0)
    prefWidth = w
    prefHeight = h
}

fun Button.iosBig(w: Int, h: Int) {
    background = Background(BackgroundFill(Color.TRANSPARENT, null, null))
    textFill = BUTTON_TEXT_COLOR
    if (w != -1) prefWidth = w.toDouble()
    if (h != -1) prefHeight = h.toDouble()
}

fun <T : Any> ListView<T>.ios() {
    background = Background(BackgroundFill(Color.WHITE, null, null))
}

abstract class BaseCell<T>: ListCell<T>() {

    abstract fun itemHeight(): Double
    abstract fun makePane(): Node?

    override fun updateItem(item: T, empty: Boolean) {
        super.updateItem(item, empty)
        text = null
        graphic = null
        if (!empty) {
            graphic = makePane()
        }
    }
    init {
        prefHeight = itemHeight()
    }
}