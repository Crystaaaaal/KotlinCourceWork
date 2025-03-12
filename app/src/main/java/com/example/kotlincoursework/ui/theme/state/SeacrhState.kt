package com.example.kotlincoursework.ui.theme.state

import dataBase.User

sealed class SeacrhState {
    object Idle : SeacrhState()
    object Loading : SeacrhState()
    data class Success(val UserList: List<User>) : SeacrhState()
    data class Error(val message: String) : SeacrhState()
}