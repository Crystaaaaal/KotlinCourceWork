package com.example.kotlincoursework.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class LoginAndRegisterViewModel: ViewModel() {

    //для хранение и обновления значения поля ввода логина при авторизации
    private val _loginTextForPhoneNumber = MutableLiveData<String>()
    val loginTextForPhoneNumber: LiveData<String> get() = _loginTextForPhoneNumber

    fun updateLoginTextForPhoneNumber(text: String) {
        _loginTextForPhoneNumber.value = text
    }

}