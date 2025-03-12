package com.example.kotlincoursework.ui.theme.state

import dataBase.Token
import dataBase.User

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val token: Token) : LoginState()
    data class Error(val message: String) : LoginState()
}
