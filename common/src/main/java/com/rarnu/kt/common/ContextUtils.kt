package com.rarnu.kt.common

import javafx.application.Platform
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.lang.System
import java.util.*
import javax.swing.SwingUtilities

fun readConfig(key: String, def: String) = ContextOperations.read(key, def)
fun writeConfig(key: String, value: String) = ContextOperations.write(key, value)
fun readConfig(key: String, def: Int) = ContextOperations.read(key, def.toString()).toInt()
fun writeConfig(key: String, value: Int) = ContextOperations.write(key, value.toString())
fun readConfig(key: String, def: Float) = ContextOperations.read(key, def.toString()).toFloat()
fun writeConfig(key: String, value: Float) = ContextOperations.write(key, value.toString())
fun readConfig(key: String, def: Long) = ContextOperations.read(key, def.toString()).toLong()
fun writeConfig(key: String, value: Long) = ContextOperations.write(key, value.toString())
fun readConfig(key: String, def: Boolean) = ContextOperations.read(key, def.toString()).toBoolean()
fun writeConfig(key: String, value: Boolean) = ContextOperations.write(key, value.toString())

fun fxMainThread(runnable: () -> Unit) = Platform.runLater { runnable() }
fun swingMainThread(runnable: () -> Unit) = SwingUtilities.invokeLater { runnable() }

private object ContextOperations {

    private val prop = Properties()
    private val cfgPath = File(System.getProperty("user.dir"), "cfg.properties")

    fun load() {
        if (cfgPath.exists()) {
            val inStream = BufferedInputStream(FileInputStream(cfgPath))
            prop.load(inStream)
            inStream.close()
        }
    }

    fun save() {
        val outStream = FileOutputStream(cfgPath)
        prop.store(outStream, "")
        outStream.close()
    }

    fun read(key: String, def: String): String {
        load()
        return prop.getProperty(key, def)
    }

    fun write(key: String, v: String) {
        load()
        prop.setProperty(key, v)
        save()
    }

}