package com.rarnu.kt

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.TelephonyManager
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.File
import java.io.InputStreamReader
import kotlin.concurrent.thread

enum class CommandProgressType { START, READLINE, READERROR, COMPLETE }

class Command {
    var commands = mutableListOf<String>()
    var runAsRoot = false

    var _progress: (CommandProgressType, String?) -> Unit = { _, _ -> }
    var _result:(String?, String?) -> Unit = { _, _ -> }


    fun progress(p:(CommandProgressType, String?) -> Unit) {
        _progress = p
    }
    fun result(r:(String?, String?) -> Unit) {
        _result = r
    }
}

object System {
    val rooted = CommandOperations.rooted
    val busyboxInstalled = CommandOperations.busyboxInstalled
    fun isEmulator(ctx: Context) = SystemOperations.isEmulator(ctx)
}

fun runCommandAsync(init: Command.() -> Unit) = thread { runCommand(init) }

fun runCommand(init: Command.() -> Unit) {
    val c = Command()
    c.init()
    CommandOperations.runCommand(c.commands, c.runAsRoot, c._progress, c._result)
}

/**
 * Created by rarnu on 3/23/16.
 */
private object CommandOperations {

    internal val rooted = arrayOf("/system/bin/su", "/system/xbin/su").any { File(it).exists() }

    internal var busyboxInstalled = arrayOf("/system/xbin/busybox", "/system/bin/busybox").any { File(it).exists() }

    fun runCommand(command: List<String>, root: Boolean, progress: (CommandProgressType, String?) -> Unit, result:(String?, String?) -> Unit) {
        var output: String? = ""
        var outError: String? = null
        val process: Process
        var rootOs: DataOutputStream? = null
        var procOutOs: BufferedReader? = null
        var procErrOs: BufferedReader? = null
        progress(CommandProgressType.START, null)
        try {
            if (root) {
                process = Runtime.getRuntime().exec("su")
                rootOs = DataOutputStream(process.outputStream)
                command.forEach { rootOs?.writeBytes("$it\n") }
                rootOs.writeBytes("exit\n")
                rootOs.flush()
            } else {
                process = Runtime.getRuntime().exec(command.toTypedArray())
            }
            procOutOs = BufferedReader(InputStreamReader(process.inputStream))
            procErrOs = BufferedReader(InputStreamReader(process.errorStream))
            val outStr = StringBuffer()
            val errStr = StringBuffer()
            var line: String?
            while (true) {
                line = procOutOs.readLine()
                if (line != null) {
                    outStr.append("$line\n")
                    progress(CommandProgressType.READLINE, line)
                } else {
                    break
                }
            }
            while (true) {
                line = procErrOs.readLine()
                if (line != null) {
                    errStr.append("$line\n")
                    progress(CommandProgressType.READERROR, line)
                } else {
                    break
                }
            }
            process.waitFor()
            output = outStr.toString().trim()
            outError = errStr.toString().trim()
        } catch(e: Exception) {
            if (e.message != null) {
                outError = e.message!!
            }
        } finally {
            rootOs?.close()
            procOutOs?.close()
            procErrOs?.close()
        }
        progress(CommandProgressType.COMPLETE, null)
        result(output, outError)
    }

}

private object SystemOperations {

    fun isEmulator(context: Context): Boolean = try {
        var ret = false
        if (context.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val imei = tm.deviceId
            if (imei != null && imei == "000000000000000") {
                ret = true
            }
        }

        if (Build.SERIAL == "" || Build.PRODUCT.contains("sdk")) {
            ret = true
        }
        if (!ret) { ret = isBlueStacks() }
        if (!ret) { ret = isGenymotion() }
        ret
    } catch (e: Exception) {
        false
    }

    private fun isBlueStacks(): Boolean = Build.MODEL.toLowerCase().contains("bluestacks")

    private fun isGenymotion(): Boolean = Build.MODEL.toLowerCase().contains("genymotion")
}