package com.example.domain.di

import com.example.datalayer.repos.ChatRepo
import com.example.domain.repoimpl.ChatRepoImpl
import com.example.domain.room.RoomController
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val mainModule = module {
    single {
        KMongo.createClient()
            .coroutine
            .getDatabase("message_db_yt")
    }
    single<ChatRepo> {
        ChatRepoImpl(get())
    }
    single {
        RoomController(get())
    }
}