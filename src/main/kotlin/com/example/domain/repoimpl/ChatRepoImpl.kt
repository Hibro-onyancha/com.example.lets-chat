package com.example.domain.repoimpl

import com.example.datalayer.models.Address
import com.example.datalayer.models.Chat
import com.example.datalayer.repos.ChatRepo
import com.mongodb.client.model.Filters
import org.litote.kmongo.combine
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq
import org.litote.kmongo.setValue

class ChatRepoImpl(
    database: CoroutineDatabase
) : ChatRepo {
    private val allChats = database.getCollection<Chat>()
    override suspend fun getAllMessages(): List<Chat> {
        val filter = Filters.elemMatch("addressList", Filters.eq("street", "Ngong Road"))

        return allChats.find().ascendingSort(Chat::time).toList()
    }

    override suspend fun sendChat(chat: Chat) {
        allChats.insertOne(chat)
    }

    override suspend fun updateChat(id: String, newChat: Chat): Boolean {
        val result = allChats.updateOne(
            Chat::id eq id,
            combine(
                setValue(Chat::message, newChat.message),
                setValue(Chat::address, newChat.address)
            )
        )
       /* val toAdd = Chat(
            message = "Updated message",
            time = "Updated time",
            userName = "Updated userName",
            address = Address(city = "New City", country = "New Country", street = "Ngong Road")
        )
        allChats.insertOne(toAdd)*/

        return result.modifiedCount > 0
    }

}