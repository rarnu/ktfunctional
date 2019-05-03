package com.rarnu.kt.ktor

import io.ktor.application.Application
import java.sql.Connection
import java.sql.DriverManager

private var connection: Connection? = null

class DB(val app: Application) {
    fun conn(): Connection {
        if (connection == null) {
            val driver = app.config("ktor.database.driver")
            val url = app.config("ktor.database.url")
            val user = app.config("ktor.database.user")
            val password = app.config("ktor.database.password")
            Class.forName(driver)
            connection = DriverManager.getConnection(url, user, password)
        }
        return connection!!
    }
}
