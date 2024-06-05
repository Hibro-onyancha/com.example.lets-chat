package com.example.domain.routes

import com.example.datalayer.models.Chat
import com.example.datalayer.models.Session
import com.example.domain.room.MemberException
import com.example.domain.room.RoomController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import kotlinx.serialization.json.Json

fun Route.chatSocket(roomController: RoomController) {
    webSocket("/chat-socket") {
        val session = call.sessions.get<Session>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session."))
            return@webSocket
        }
        try {
            roomController.onJoin(
                username = session.userName, sessionId = session.sessionId, socket = this
            )

            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    try {
                        val chat = Json.decodeFromString<Chat>(frame.readText())
                        roomController.sendMessage(chat)

                    } catch (e: Exception) {
                        println(e)
                    }
                }
            }
        } catch (e: MemberException) {
            call.respond(HttpStatusCode.Conflict)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            roomController.tryDisconnect(session.userName)
        }
    }
}

fun Route.getAllMessages(roomController: RoomController) {
    get("/chats") {
        call.respond(
            HttpStatusCode.OK, roomController.getAllMessages()
        )
    }
}