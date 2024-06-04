package com.example.datalayer.repos

import com.example.datalayer.models.Chat

interface ChatRepo {

    suspend fun getAllMessages(): List<Chat>
    suspend fun sendChat(chat: Chat)
}