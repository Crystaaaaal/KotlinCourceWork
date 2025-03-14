package com.example.kotlincoursework.ui.theme.state

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val success: Boolean) : LoginState()
    data class Error(val message: String) : LoginState()
}
