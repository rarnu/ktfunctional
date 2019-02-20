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
import java.awt.*
import java.awt.font.FontRenderContext
import java.util.*
import javax.swing.JWindow
import kotlin.concurrent.thread

object Toast {

    const val MESSAGE = 0
    const val SUCCESS = 1
    const val ERROR = 2

    fun fxMakeText(message: String, delay: Int = 1500, fadeIn: Int = 250, fadeOut: Int = 250) {
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

    fun swingMakeText(parent: Window, message: String, delay: Int = 1500, type: Int = MESSAGE) = Toast(parent, message, delay, type).showToast()

    private class Toast : JWindow {

        companion object {
            const val MESSAGE = 0
            const val SUCCESS = 1
            const val ERROR = 2
        }

        private var message = ""
        private val ins = java.awt.Insets(12, 24, 12, 24)
        private var period = 1500

        private lateinit var backgroundColor: java.awt.Color
        private lateinit var foregroundColor: java.awt.Color

        constructor(parent: Window, message: String, period: Int = 1500) : this(parent, message, period, 0)
        constructor(parent: Window, message: String, period: Int, type: Int) : super(parent) {
            this.message = message
            this.period = period
            size = getStringSize(message)
            setLocationRelativeTo(parent)
            installTheme(type)
        }

        override fun paint(g: Graphics) {
            val g2 = g as Graphics2D
            val oldComposite = g2.composite
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
            val fm = g2.fontMetrics
            g2.color = backgroundColor
            g2.fillRoundRect(0, 0, width, height, 12, 12)
            g2.color = foregroundColor
            g2.drawString(message, ins.left, fm.ascent + ins.top)
            g2.composite = oldComposite
        }

        fun showToast() {
            this.isVisible = true
            revalidate()
            repaint(0, 0, size.width, size.height)
            val timer = Timer()
            timer.schedule(object : TimerTask() { override fun run() { isVisible = false } }, period.toLong())
        }

        fun setMessage(message: String) {
            this.message = message
            val size = getStringSize(message)
            setSize(size)
            revalidate()
            repaint(0, 0, size.width, size.height)
            if (!isVisible) { showToast() }
        }

        private fun installTheme(type: Int) = when (type) {
            MESSAGE -> {
                backgroundColor = java.awt.Color(0x515151)
                foregroundColor = java.awt.Color.WHITE
            }
            SUCCESS -> {
                backgroundColor = java.awt.Color(223, 240, 216)
                foregroundColor = java.awt.Color(49, 112, 143)
            }
            ERROR -> {
                backgroundColor = java.awt.Color(242, 222, 222)
                foregroundColor = java.awt.Color(221, 17, 68)
            }
            else -> {
                backgroundColor = java.awt.Color(0x515151)
                foregroundColor = java.awt.Color.WHITE
            }
        }


        private fun getStringSize(text: String): Dimension {
            val renderContext = FontRenderContext(null, true, false)
            val bounds = font.getStringBounds(text, renderContext)
            val width = bounds.width.toInt() + 2 * ins.left
            val height = bounds.height.toInt() + ins.top * 2
            return Dimension(width, height)
        }
    }
}