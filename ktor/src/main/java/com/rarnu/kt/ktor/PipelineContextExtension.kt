package com.rarnu.kt.ktor

import io.ktor.application.ApplicationCall
import io.ktor.application.application
import io.ktor.application.call
import io.ktor.sessions.get
import io.ktor.sessions.sessions
import io.ktor.sessions.set
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.pipeline.PipelineContext

inline fun <reified T> PipelineContext<*, ApplicationCall>.session(init: () -> T): T {
    var ses: T?
    try {
        ses = call.sessions.get<T>()
        if (ses == null) {
            ses = init()
            call.sessions.set(ses)
        }
    } catch (th: Throwable) {
        ses = init()
        call.sessions.set(ses)
    }
    return ses!!
}

@UseExperimental(KtorExperimentalAPI::class)
fun PipelineContext<*, ApplicationCall>.config(key: String) = application.config(key)
