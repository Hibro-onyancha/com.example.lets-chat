package com.example.domain.routes

import com.example.datalayer.models.Chat
import com.example.datalayer.models.Session
import com.example.datalayer.models.UpdateChatRequest
import com.example.domain.room.MemberException
import com.example.domain.room.RoomController
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import jdk.internal.net.http.common.Log
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

fun Route.updateChat(roomController: RoomController) {
    post("/chats/update-chat") {
        val request = call.receive<UpdateChatRequest>()

        val updated = roomController.updateChat(id = request.id, chat = request.newChat)
        if (updated) {
            call.respond(HttpStatusCode.OK, "Chat updated successfully")
        } else {
            call.respond(HttpStatusCode.NotFound, "Chat not found")
            if (request.newChat.id != "") {
                println("received your chat ${request.id}")
            } else {
               println("error chat not received")
            }
        }
    }
}


