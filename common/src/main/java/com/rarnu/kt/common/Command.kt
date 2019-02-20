package com.rarnu.kt.common

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import kotlin.concurrent.thread

enum class CommandProgressType { START, READLINE, READERROR, COMPLETE }

class Command {
    var commands = mutableListOf<String>()
    var runAsRoot = false
    var password = ""

    var _progress: (CommandProgressType, String) -> Unit = { _, _ -> }
    var _result: (String, String) -> Unit = { _, _ -> }

    fun progress(p: (type: CommandProgressType, value: String) -> Unit) {
        _progress = p
    }

    fun result(r: (output: String, error: String) -> Unit) {
        _result = r
    }
}

fun runCommandAsync(init: Command.() -> Unit) = thread { runCommand(init) }

fun runCommand(init: Command.() -> Unit): CommandResult {
    val c = Command()
    c.init()
    return CommandOperations.runCommand(c.commands, c.runAsRoot, c.password, c._progress, c._result)
}

/**
 * Created by rarnu on 3/23/16.
 */
internal object CommandOperations {

    fun runCommand(command: MutableList<String>, root: Boolean, password: String, progress: (CommandProgressType, String) -> Unit, result: (String, String) -> Unit): CommandResult {
        var output = ""
        var outError = ""
        val process: Process
        var rootOs: DataOutputStream? = null
        var procOutOs: BufferedReader? = null
        var procErrOs: BufferedReader? = null
        progress(CommandProgressType.START, "")
        try {
            if (root) {
                command.add(0, "sudo")
                process = Runtime.getRuntime().exec(command.toTypedArray())
                rootOs = DataOutputStream(process.outputStream)
                rootOs.writeBytes("$password\n")
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
        } catch (e: Exception) {
            if (e.message != null) {
                outError = e.message!!
            }
        } finally {
            rootOs?.close()
            procOutOs?.close()
            procErrOs?.close()
        }
        progress(CommandProgressType.COMPLETE, "")
        result(output, outError)
        return CommandResult(output, outError)
    }

}

data class CommandResult(val output: String, val error: String)