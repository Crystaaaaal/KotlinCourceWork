package com.example.kotlincoursework.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.kotlincoursework.API.ServerRepository
import dataBase.LoginUser
import dataBase.RegistrationUserInfo
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class viewModel : ViewModel() {
    var topBarText by mutableStateOf("Мессенджер")
        private set

    private val _items = mutableStateListOf<String>()
    val items: List<String> get() = _items

    fun addItem(item: String) {
        _items.add(item)
    }

    fun updateTopBarText(newText: String) {
        topBarText = newText
    }

    //для хранение и обновления значения поля ввода логина при авторизации
    // Состояние для текста номера телефона
    private val _loginTextForPhoneNumber = MutableStateFlow("+7")
    val loginTextForPhoneNumber: StateFlow<String> get() = _loginTextForPhoneNumber

    // Функция для обновления текста
    fun updateLoginTextForPhoneNumber(text: String) {
        _loginTextForPhoneNumber.value = text
    }

    //для хранения и обновления значение поля пароля при авторизации
    private val _textForPassword = MutableStateFlow("")
    val loginTextForPassword: StateFlow<String> get() = _textForPassword

    // Функция для обновления текста
    fun updateTextForPassword(text: String) {
        _textForPassword.value = text
    }

    // Для номера телефона при регистрации
    private val _textForRegisterPhoneNumber = MutableStateFlow("+7")
    val textForRegisterPhoneNumber: StateFlow<String> get() = _textForRegisterPhoneNumber

    fun updateTextForRegisterPhoneNumber(text: String) {
        _textForRegisterPhoneNumber.value = text
    }

    // Для логина при регистрации
    private val _textForRegisterLogin = MutableStateFlow("")
    val textForRegisterLogin: StateFlow<String> get() = _textForRegisterLogin

    fun updateTextForRegisterLogin(text: String) {
        _textForRegisterLogin.value = text
    }

    // Для пароля при регистрации
    private val _textForRegisterPassword = MutableStateFlow("")
    val textForRegisterPassword: StateFlow<String> get() = _textForRegisterPassword

    fun updateTextForRegisterPassword(text: String) {
        _textForRegisterPassword.value = text
    }

    // Для фамилии при регистрации
    private val _textForRegisterSecondName = MutableStateFlow("")
    val textForRegisterSecondName: StateFlow<String> get() = _textForRegisterSecondName

    fun updateTextForRegisterSecondName(text: String) {
        _textForRegisterSecondName.value = text
    }

    // Для имени при регистрации
    private val _textForRegisterName = MutableStateFlow("")
    val textForRegisterName: StateFlow<String> get() = _textForRegisterName

    fun updateTextForRegisterName(text: String) {
        _textForRegisterName.value = text
    }

    // Для отчества при регистрации
    private val _textForRegisterFatherName = MutableStateFlow("")
    val textForRegisterFatherName: StateFlow<String> get() = _textForRegisterFatherName

    fun updateTextForRegisterFatherName(text: String) {
        _textForRegisterFatherName.value = text
    }

    private fun buildUser(): RegistrationUserInfo {
        val user = RegistrationUserInfo(
            textForRegisterPhoneNumber.value,
            textForRegisterLogin.value,
            textForRegisterPassword.value,
            textForRegisterSecondName.value,
            textForRegisterName.value,
            textForRegisterFatherName.value
        )
        Log.d("viewModel: buildUser", "Пользователь: $user")
        return user
    }

    fun registerUser(): Boolean {
        Log.i("viewModel: registerUser", "Регистрация: начало")
        val result = CompletableDeferred<Boolean>() // Создаем CompletableDeferred для хранения результата

        CoroutineScope(Dispatchers.IO).launch {
            val user = buildUser()
            val isUserRegister = async { ServerRepository().registrationUser(user) }.await()

            withContext(Dispatchers.IO) {
                if (isUserRegister) {
                    Log.i("viewModel: registerUser", "Регистрация прошла успешно: конец")
                    result.complete(true) // Успешная регистрация
                } else {
                    Log.e("viewModel: registerUser", "Регистрация прошла не успешно: конец")
                    result.complete(false) // Неуспешная регистрация
                }
            }
        }

        // Блокируем текущий поток и ждем завершения CompletableDeferred
        return runBlocking { result.await() }
    }

    private fun buildlLoginUser(): LoginUser {
        val user = LoginUser(
            phoneNumber = loginTextForPhoneNumber.value,
            password = loginTextForPassword.value
        )
        return user

    }

    fun loginUser(): Boolean {
        Log.i("viewModel: loginUser", "Авторизация: начало")
        val result = CompletableDeferred<Boolean>()

        CoroutineScope(Dispatchers.IO).launch {
            val user = buildlLoginUser()
            val token = async { ServerRepository().loginUser(user) }.await()

            withContext(Dispatchers.IO) {
                if (token.token != "") {
                    Log.i("viewModel: loginUser", "Авторизация прошла успешно: конец")
                    result.complete(true)
                } else {
                    Log.e("viewModel: loginUser", "Авторизация прошла не успешно: конец")
                    result.complete(false)
                }
            }
        }

        return runBlocking { result.await() }
    }
}