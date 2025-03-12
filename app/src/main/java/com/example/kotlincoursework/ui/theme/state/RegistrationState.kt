package com.example.kotlincoursework.ui.theme.state

sealed class RegistrationState {
    object Idle : RegistrationState()
    object Loading : RegistrationState()
    data class Success(val isRegistered: Boolean) : RegistrationState()
    data class Error(val message: String) : RegistrationState()
}