@file:Suppress("unused")

package com.rarnu.kt.ktor

import java.sql.ResultSet
import java.sql.Timestamp

fun ResultSet.toJsonArray(): String {
    var str = "["
    if (first()) {
        do {
            var item = "{"
            for (i in 0 until metaData.columnCount) {
                item += "\"${metaData.getColumnName(i)}\": \"${getString(i)}\","
            }
            item += "},"
            str += item
        } while (next())
    }
    str = str.trimEnd(',')
    str += "]"
    return str
}

fun ResultSet.forEach(operator:(ResultSet) -> Unit) {
    if (first()) {
        do {
            operator(this)
        } while (next())
    }
}

fun ResultSet.firstRecord(operator: (ResultSet) -> Unit) {
    if (first()) {
        operator(this)
    }
}

fun ResultSet.string(columnName: String): String = getString(findColumn(columnName))
fun ResultSet.int(columnName: String): Int = getInt(findColumn(columnName))
fun ResultSet.double(columnName: String): Double = getDouble(findColumn(columnName))
fun ResultSet.long(columnName: String): Long = getLong(findColumn(columnName))
fun ResultSet.timestamp(columnName: String): Timestamp = getTimestamp(findColumn(columnName))