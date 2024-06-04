package com.example.domain.repoimpl

import com.example.datalayer.models.Chat
import com.example.datalayer.repos.ChatRepo
import org.litote.kmongo.coroutine.CoroutineDatabase

class ChatRepoImpl(
    database: CoroutineDatabase
) : ChatRepo {
    private val allChats = database.getCollection<Chat>()
    override suspend fun getAllMessages(): List<Chat> {
        return allChats.find().ascendingSort(Chat::time).toList()
    }

    override suspend fun sendChat(chat: Chat) {
        allChats.insertOne(chat)
    }
}