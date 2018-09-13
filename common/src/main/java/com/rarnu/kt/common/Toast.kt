package com.rarnu.kt.common

import javafx.animation.KeyFrame
import javafx.animation.KeyValue
import javafx.animation.Timeline
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.text.Font
import javafx.scene.text.Text
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.util.Duration
import kotlin.concurrent.thread

object Toast {

    fun makeText(message: String?, delay: Int = 1500, fadeIn: Int = 250, fadeOut: Int = 250) {
        val toast = Stage()
        toast.isResizable = false
        toast.isAlwaysOnTop = true
        toast.initStyle(StageStyle.TRANSPARENT)
        val text = Text(message)
        text.font = Font.font("Verdana", 20.0)
        text.fill = Color.WHITE
        val root = StackPane(text)
        root.background = Background(BackgroundFill(BUTTON_TEXT_COLOR, CornerRadii(10.0), null))
        root.padding = Insets(20.0, 20.0, 20.0, 20.0)
        val scene = Scene(root)
        scene.fill = Color.TRANSPARENT
        toast.scene = scene
        toast.show()

        val fadeInTimeline = Timeline()
        val fadeInKey = KeyFrame(Duration.millis(fadeIn.toDouble()), KeyValue(toast.scene.root.opacityProperty(), 1))
        fadeInTimeline.keyFrames.add(fadeInKey)
        fadeInTimeline.setOnFinished {
            thread {
                Thread.sleep(delay.toLong())
                val fadeOutTimeline = Timeline()
                val fadeOutKey = KeyFrame(Duration.millis(fadeOut.toDouble()), KeyValue(toast.scene.root.opacityProperty(), 0))
                fadeOutTimeline.keyFrames.add(fadeOutKey)
                fadeOutTimeline.setOnFinished { toast.close() }
                fadeOutTimeline.play()
            }
        }
        fadeInTimeline.play()
    }
}