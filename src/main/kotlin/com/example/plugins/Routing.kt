package com.example.plugins

import com.example.domain.di.mainModule
import com.example.domain.room.RoomController
import com.example.domain.routes.chatSocket
import com.example.domain.routes.getAllMessages
import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.koin.core.context.startKoin
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    startKoin {
        modules(mainModule)
    }
    val roomController by inject<RoomController>()
    install(Routing) {
        chatSocket(roomController)
        getAllMessages(roomController)
    }
}
