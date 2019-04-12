package com.rarnu.kt.common

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.lang.System
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread

enum class CommandProgressType { START, READLINE, READERROR, COMPLETE }

class Command {
    var commands = mutableListOf<String>()
    var runAsRoot = false
    var password = ""
    var timeout = 3000L

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
    return CommandOperations.runCommand(c.commands, c.runAsRoot, c.password, c.timeout, c._progress, c._result)
}

/**
 * Created by rarnu on 3/23/16.
 */
internal object CommandOperations {

    fun runCommand(command: MutableList<String>, root: Boolean, password: String, timeout: Long, progress: (CommandProgressType, String) -> Unit, result: (String, String) -> Unit): CommandResult {
        var output = ""
        var outError = ""
        var process: Process? = null
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
            val start = System.currentTimeMillis()
            while (true) {
                if (procOutOs.ready()) {
                    line = procOutOs.readLine()
                    println(line)
                    if (line != null) {
                        outStr.append("$line\n")
                        progress(CommandProgressType.READLINE, line)
                        continue
                    }
                }
                if (procErrOs.ready()) {
                    line = procErrOs.readLine()
                    if (line != null) {
                        errStr.append("$line\n")
                        progress(CommandProgressType.READERROR, line)
                        continue
                    }
                }

                if (process != null) {
                    try {
                        process.exitValue()
                        break
                    } catch (e: Exception) {
                    }
                }
                if (System.currentTimeMillis() - start > timeout) {
                    errStr.append("Timeout\n")
                    break
                }
                try {
                    TimeUnit.MILLISECONDS.sleep(500)
                } catch (e: Exception) {
                }

            }

            // process.waitFor()
            output = outStr.toString().trim()
            outError = errStr.toString().trim()
        } catch (e: Exception) {
            if (e.message != null) {
                outError = e.message!!
            }
        } finally {
            if (root) { rootOs?.close() }
            procOutOs?.close()
            procErrOs?.close()
            if (process != null) {
                try {
                    process.destroy()
                } catch (e: Throwable) {

                }
            }
        }
        progress(CommandProgressType.COMPLETE, "")
        result(output, outError)
        return CommandResult(output, outError)
    }
}

data class CommandResult(val output: String, val error: String)