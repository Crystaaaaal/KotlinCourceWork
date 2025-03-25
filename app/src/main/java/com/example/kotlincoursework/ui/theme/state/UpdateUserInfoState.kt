package com.example.kotlincoursework.ui.theme.state

import dataBase.ActiveUser

sealed class UpdateUserInfoState {
    object Idle : UpdateUserInfoState()
    object Loading : UpdateUserInfoState()
    data class Success(val success: Boolean) : UpdateUserInfoState()
    data class Error(val message: String) : UpdateUserInfoState()
}