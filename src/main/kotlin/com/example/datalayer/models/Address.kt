package com.example.datalayer.models

import kotlinx.serialization.Serializable
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId


@Serializable
data class Address(
    @BsonId var id: String = ObjectId().toString(),
    val country: String,
    val city: String,
    val street: String
)
