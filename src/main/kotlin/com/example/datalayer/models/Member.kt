package com.example.datalayer.models

import io.ktor.websocket.*

data class Member(
    val userName: String,
    val sessionId: String,
    val webSocketSession: WebSocketSession
)