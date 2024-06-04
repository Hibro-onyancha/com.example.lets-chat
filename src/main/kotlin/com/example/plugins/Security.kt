package com.example.plugins

import com.example.datalayer.models.Session
import io.ktor.server.application.*
import io.ktor.server.sessions.*
import io.ktor.util.*

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<Session>("SESSION")
    }

    intercept(ApplicationCallPipeline.Features) {
        if (call.sessions.get<Session>() == null) {
            val username = call.parameters["username"] ?: "Guest"
            call.sessions.set(Session(userName = username, sessionId = generateNonce()))//generate a special key for this session
        }
    }
}
