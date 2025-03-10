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
import dataBase.UserRegistrationRequest
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



    // Для номера телефона при авторизации
    private val _isLoginPhoneNumberValid = MutableStateFlow(false)
    val isLoginPhoneNumberValid: StateFlow<Boolean> get() = _isLoginPhoneNumberValid

    // Для пароля при авторизации
    private val _isLoginPasswordValid = MutableStateFlow(false)
    val isLoginPasswordValid: StateFlow<Boolean> get() = _isLoginPasswordValid

    // Для номера телефона при регистрации
    private val _isRegisterPhoneNumberValid = MutableStateFlow(false)
    val isRegisterPhoneNumberValid: Boolean get() = _isRegisterPhoneNumberValid.value

    // Для логина при регистрации
    private val _isRegisterLoginValid = MutableStateFlow(false)
    val isRegisterLoginValid: Boolean get() = _isRegisterLoginValid.value

    // Для пароля при регистрации
    private val _isRegisterPasswordValid = MutableStateFlow(false)
    val isRegisterPasswordValid: Boolean get() = _isRegisterPasswordValid.value

    // Для фамилии при регистрации
    private val _isRegisterSecondNameValid = MutableStateFlow(false)
    val isRegisterSecondNameValid: Boolean get() = _isRegisterSecondNameValid.value

    // Для имени при регистрации
    private val _isRegisterNameValid = MutableStateFlow(false)
    val isRegisterNameValid: Boolean get() = _isRegisterNameValid.value

    // Для отчества при регистрации
    private val _isRegisterFatherNameValid = MutableStateFlow(false)
    val isRegisterFatherNameValid: Boolean get() = _isRegisterFatherNameValid.value


    //для хранение и обновления значения поля ввода логина при авторизации
    // Состояние для текста номера телефона
    private val _loginTextForPhoneNumber = MutableStateFlow("+7")
    val loginTextForPhoneNumber: StateFlow<String> get() = _loginTextForPhoneNumber

    //для хранения и обновления значение поля пароля при авторизации
    private val _textForPassword = MutableStateFlow("")
    val loginTextForPassword: StateFlow<String> get() = _textForPassword

    // Для номера телефона при регистрации
    private val _textForRegisterPhoneNumber = MutableStateFlow("+7")
    val textForRegisterPhoneNumber: StateFlow<String> get() = _textForRegisterPhoneNumber

    // Для логина при регистрации
    private val _textForRegisterLogin = MutableStateFlow("")
    val textForRegisterLogin: StateFlow<String> get() = _textForRegisterLogin

    // Для пароля при регистрации
    private val _textForRegisterPassword = MutableStateFlow("")
    val textForRegisterPassword: StateFlow<String> get() = _textForRegisterPassword

    // Для фамилии при регистрации
    private val _textForRegisterSecondName = MutableStateFlow("")
    val textForRegisterSecondName: StateFlow<String> get() = _textForRegisterSecondName

    // Для имени при регистрации
    private val _textForRegisterName = MutableStateFlow("")
    val textForRegisterName: StateFlow<String> get() = _textForRegisterName

    // Для отчества при регистрации
    private val _textForRegisterFatherName = MutableStateFlow("")
    val textForRegisterFatherName: StateFlow<String> get() = _textForRegisterFatherName



    // Для номера телефона при авторизации
    fun updateLoginTextForPhoneNumber(text: String) {
        _loginTextForPhoneNumber.value = text
        _isLoginPhoneNumberValid.value = validatePhoneNumber(text)
    }

    // Для пароля при авторизации
    fun updateTextForPassword(text: String) {
        _textForPassword.value = text
        _isLoginPasswordValid.value = validatePassword(text)
    }

    // Для номера телефона при регистрации
    fun updateTextForRegisterPhoneNumber(text: String) {
        _textForRegisterPhoneNumber.value = text
        _isRegisterPhoneNumberValid.value = validatePhoneNumber(text)
    }

    // Для логина при регистрации
    fun updateTextForRegisterLogin(text: String) {
        _textForRegisterLogin.value = text
        _isRegisterLoginValid.value = validateLogin(text)
    }

    // Для пароля при регистрации
    fun updateTextForRegisterPassword(text: String) {
        _textForRegisterPassword.value = text
        _isRegisterPasswordValid.value = validatePassword(text)
    }

    // Для фамилии при регистрации
    fun updateTextForRegisterSecondName(text: String) {
        _textForRegisterSecondName.value = text
        _isRegisterSecondNameValid.value = validateName(text)
    }

    // Для имени при регистрации
    fun updateTextForRegisterName(text: String) {
        _textForRegisterName.value = text
        _isRegisterNameValid.value = validateName(text)
    }

    // Для отчества при регистрации
    fun updateTextForRegisterFatherName(text: String) {
        _textForRegisterFatherName.value = text
        _isRegisterFatherNameValid.value = validateName(text)
    }





    // Проверка номера телефона
    private fun validatePhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length == 12 && phoneNumber.startsWith("+")
    }

    // Проверка пароля
    private fun validatePassword(password: String): Boolean {
        return password.length >= 5
    }

    // Проверка логина
    private fun validateLogin(login: String): Boolean {
        return login.length >= 3
    }

    // Проверка имени, фамилии, отчества
    private fun validateName(name: String): Boolean {
        return name.isNotBlank() && name.length in 2 .. 30
    }



    fun isRegistrationFormValid(): Boolean {
        return _isRegisterPhoneNumberValid.value &&
                _isRegisterLoginValid.value &&
                _isRegisterPasswordValid.value &&
                _isRegisterSecondNameValid.value &&
                _isRegisterNameValid.value &&
                _isRegisterFatherNameValid.value
    }

    fun isLoginFormValid(): Boolean {
        return _isLoginPhoneNumberValid.value &&
                _isLoginPasswordValid.value
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

        if (!isRegistrationFormValid()) {
            Log.i("viewModel: registerUser", "Невалидные данные")
            Log.e("viewModel: registerUser", "Регистрация прошла не успешно: конец")
            return false
        }
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
        if (!isLoginFormValid()) {
            Log.i("viewModel: loginUser", "Невалидные данные")
            Log.e("viewModel: loginUser", "Авторизация прошла не успешно: конец")
            return false
        }
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