package com.example.kotlincoursework.ui.theme.state

import dataBase.ActiveUser


sealed class GetUserInfoState {
    object Idle : GetUserInfoState()
    object Loading : GetUserInfoState()
    data class Success(val success: ActiveUser) : GetUserInfoState()
    data class Error(val message: String) : GetUserInfoState()
}