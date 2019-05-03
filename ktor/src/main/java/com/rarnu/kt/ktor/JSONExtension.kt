package com.rarnu.kt.ktor

import org.json.JSONArray
import org.json.JSONObject

fun JSONArray.forEach(operation: (Int, JSONObject) -> Unit) {
    for (i in 0 until this.length()) {
        operation(i, this.getJSONObject(i))
    }
}

fun JSONArray.forEachString(operation: (Int, String) -> Unit) {
    for (i in 0 until this.length()) {
        operation(i, this.getString(i))
    }
}

fun JSONArray.stringIndexOf(str: String): Int {
    var ret = -1
    for (i in 0 until this.length()) {
        if (this.getString(i) == str) {
            ret = i
            break
        }
    }
    return ret
}

fun JSONArray.isMatchEveryString(arr: JSONArray): Boolean {
    var hitCount = 0
    if (this.length() != arr.length()) {
        return false
    }
    for (i in 0 until this.length()) {
        if (arr.stringIndexOf(this.getString(i)) != -1) {
            hitCount++
        }
    }
    return hitCount == this.length()
}

fun JSONArray.isEveryStringContainedBy(str: String): Boolean {
    var hitCount = 0
    for (i in 0 until this.length()) {
        if (str.contains(this.getString(i))) {
            hitCount++
        }
    }
    return hitCount == this.length()
}