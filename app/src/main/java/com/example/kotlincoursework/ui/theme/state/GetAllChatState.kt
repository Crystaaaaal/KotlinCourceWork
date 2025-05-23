package com.example.kotlincoursework.ui.theme.state

import dataBase.ChatRemote
import dataBase.MessagesRemote

sealed class GetAllChatState {
    object Idle : GetAllChatState()
    object Loading : GetAllChatState()
    data class Success(val success: ChatRemote) : GetAllChatState()
    data class Error(val message: String) : GetAllChatState()
}