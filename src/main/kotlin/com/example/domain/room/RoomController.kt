package com.example.domain.room

import com.example.datalayer.models.Chat
import com.example.datalayer.models.Member
import com.example.datalayer.repos.ChatRepo
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.concurrent.ConcurrentHashMap

class RoomController(
    private val chatRepo: ChatRepo
) {
    private val members = ConcurrentHashMap<String, Member>()

    fun onJoin(
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(username)) {
            throw MemberException()
        }
        members[username] = Member(
            userName = username,
            sessionId = sessionId,
            webSocketSession = socket
        )
    }

    suspend fun sendMessage(chat: Chat) {
        members.values.forEach { member ->
            chatRepo.sendChat(chat)
            val parsedMessage = Json.encodeToString(chat)
            member.webSocketSession.send(Frame.Text(parsedMessage))
        }
    }

    suspend fun getAllMessages(): List<Chat> {
        return chatRepo.getAllMessages()
    }

    suspend fun tryDisconnect(username: String) {
        members[username]?.webSocketSession?.close()
        if (members.containsKey(username)) {
            members.remove(username)
        }
    }

    suspend fun updateChat(id: String, chat: Chat) :Boolean{
        return chatRepo.updateChat(id, chat)
    }

}