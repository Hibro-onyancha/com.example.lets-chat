package com.example.domain.repoimpl

import com.example.datalayer.models.Chat
import com.example.datalayer.repos.ChatRepo
import com.mongodb.client.model.Filters
import org.litote.kmongo.coroutine.CoroutineDatabase

class ChatRepoImpl(
    database: CoroutineDatabase
) : ChatRepo {
    private val allChats = database.getCollection<Chat>()
    override suspend fun getAllMessages(): List<Chat> {
        val filter = Filters.elemMatch("addressList", Filters.eq("street", "Ngong Road"))

        return allChats.find(filter).ascendingSort(Chat::time).toList()
    }

    override suspend fun sendChat(chat: Chat) {
        allChats.insertOne(chat)
    }
}