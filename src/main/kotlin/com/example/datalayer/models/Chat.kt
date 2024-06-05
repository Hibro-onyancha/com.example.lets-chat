package com.example.datalayer.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId

@Serializable
data class Chat(
    @BsonId var id: String = ObjectId().toString(),
    var message: String,
    var time: String,
    var userName: String,
    val address: Address = Address(city = "Nairobi", country = "kenya", street = "nagware"),
    val addressList: List<Address> = listOf()
)