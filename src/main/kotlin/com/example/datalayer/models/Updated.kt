package com.example.datalayer.models

import kotlinx.serialization.Serializable

@Serializable
data class UpdateChatRequest(val id: String, val newChat: Chat)
