package com.example.kotlincoursework.DB.Models

import kotlinx.serialization.Serializable

data class Chat(
    val chatId: Long = 0,
    val userPhone: String,
    val contactPhone: String,
    val createdAt: Long = System.currentTimeMillis(),
    val lastMessageAt: Long? = null
)

