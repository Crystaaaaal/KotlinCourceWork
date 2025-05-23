package com.example.kotlincoursework.ui.theme.state

import dataBase.ActiveUser
import dataBase.MessagesRemote


sealed class GetMessagesState {
    object Idle : GetMessagesState()
    object Loading : GetMessagesState()
    data class Success(val success: MessagesRemote) : GetMessagesState()
    data class Error(val message: String) : GetMessagesState()
}