package com.rarnu.kt.ktor

import io.ktor.http.content.*
import java.io.File

suspend fun MultiPartData.save(field: String, dest: File): Boolean {
    var ret = false
    this.forEachPart {
        println("part = $it")
        if (it is PartData.FileItem) {
            println("part.name = ${it.name}")
            if (it.name == field) {
                println("save part")
                it.streamProvider().use { input ->
                    dest.outputStream().buffered().use { output ->
                        ret = input.copyToSuspend(output) > 0
                    }
                }
            }
            it.dispose()
        }
    }
    return ret
}

suspend fun PartData.FileItem.save(dest: File): Boolean {
    var ret = false
    this.streamProvider().use { input ->
        dest.outputStream().buffered().use { output ->
            ret = input.copyToSuspend(output) > 0
        }
    }
    return ret
}

suspend fun MultiPartData.value(name: String): String? {
    var ret: String? = null
    val list = this.readAllParts().filter { it.name == name }
    if (list.isNotEmpty()) {
        val item = list[0]
        if (item is PartData.FormItem) {
            ret = item.value
        }
    }
    return ret
}

suspend fun MultiPartData.file(name: String): PartData.FileItem? {
    var ret: PartData.FileItem? = null
    val list = this.readAllParts().filter { it.name == name }
    if (list.isNotEmpty()) {
        val item = list[0]
        if (item is PartData.FileItem) {
            ret = item
        }
    }
    return ret
}

