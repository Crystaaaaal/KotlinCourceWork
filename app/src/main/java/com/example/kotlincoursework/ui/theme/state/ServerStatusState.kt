package com.example.kotlincoursework.ui.theme.state

sealed class ServerState {
    object Idle : ServerState()
    object Loading : ServerState()
    data class Success(val isServerOnline: Boolean) : ServerState()
    data class Error(val message: String) : ServerState()
}
