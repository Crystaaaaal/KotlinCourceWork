package com.example.kotlincoursework.viewModel

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoursework.API.ServerRepository
import com.example.kotlincoursework.ui.theme.state.LoginState
import com.example.kotlincoursework.ui.theme.state.RegistrationState
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import dataBase.LoginUser
import dataBase.RegistrationUserInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class viewModel(
    private val applicationContext: Context) : ViewModel() {

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

    // Для текста номера телефона при авторизации
    private val _loginTextForPhoneNumber = MutableStateFlow("+7")
    val loginTextForPhoneNumber: StateFlow<String> get() = _loginTextForPhoneNumber

    // Для пароля при авторизации
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

    //Для текста поиска в чатах
    private val _textForSearch = MutableStateFlow("")
    val textForSearch: StateFlow<String> get() = _textForSearch

    //Обновление текста поиска
    fun updateTextForSearch(text:String){
        _textForSearch.value = text
    }

    // Обновление текста номера телефона при авторизации
    fun updateLoginTextForPhoneNumber(text: String) {
        _loginTextForPhoneNumber.value = text
        _isLoginPhoneNumberValid.value = validatePhoneNumber(text)
    }

    // Обновление пароля при авторизации
    fun updateTextForPassword(text: String) {
        _textForPassword.value = text
        _isLoginPasswordValid.value = validatePassword(text)
    }

    // Обновление номера телефона при регистрации
    fun updateTextForRegisterPhoneNumber(text: String) {
        _textForRegisterPhoneNumber.value = text
        _isRegisterPhoneNumberValid.value = validatePhoneNumber(text)
    }

    // Обновление логина при регистрации
    fun updateTextForRegisterLogin(text: String) {
        _textForRegisterLogin.value = text
        _isRegisterLoginValid.value = validateLogin(text)
    }

    // Обновление пароля при регистрации
    fun updateTextForRegisterPassword(text: String) {
        _textForRegisterPassword.value = text
        _isRegisterPasswordValid.value = validatePassword(text)
    }

    // Обновление фамилии при регистрации
    fun updateTextForRegisterSecondName(text: String) {
        _textForRegisterSecondName.value = text
        _isRegisterSecondNameValid.value = validateName(text)
    }

    // Обновление имени при регистрации
    fun updateTextForRegisterName(text: String) {
        _textForRegisterName.value = text
        _isRegisterNameValid.value = validateName(text)
    }

    // Обновление отчества при регистрации
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
        return name.isNotBlank() && name.length in 2..30
    }

    // Проверка валидности формы регистрации
    fun isRegistrationFormValid(): Boolean {
        return _isRegisterPhoneNumberValid.value &&
                _isRegisterLoginValid.value &&
                _isRegisterPasswordValid.value &&
                _isRegisterSecondNameValid.value &&
                _isRegisterNameValid.value &&
                _isRegisterFatherNameValid.value
    }

    // Проверка валидности формы авторизации
    fun isLoginFormValid(): Boolean {
        return _isLoginPhoneNumberValid.value &&
                _isLoginPasswordValid.value
    }

    // Сборка данных пользователя для регистрации
    private fun buildUser(): RegistrationUserInfo {
        return RegistrationUserInfo(
            textForRegisterPhoneNumber.value,
            textForRegisterLogin.value,
            textForRegisterPassword.value,
            textForRegisterSecondName.value,
            textForRegisterName.value,
            textForRegisterFatherName.value
        )
    }

    // Сборка данных пользователя для авторизации
    private fun buildLoginUser(): LoginUser {
        return LoginUser(
            phoneNumber = loginTextForPhoneNumber.value,
            password = loginTextForPassword.value
        )
    }

    // Состояние регистрации
    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> get() = _registrationState

    // Состояние авторизации
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    // Состояние поиска
    private val _searchState = MutableStateFlow<SeacrhState>(SeacrhState.Idle)
    val searchState: StateFlow<SeacrhState> get() = _searchState

    fun resetLoginState() {
        _loginState.value = LoginState.Idle // Сбрасываем состояние
    }

    fun resetRegistrationState(){
        _registrationState.value = RegistrationState.Idle
    }

    fun resetSearchState(){
        _searchState.value = SeacrhState.Idle
    }
    // Регистрация пользователя
    fun registerUser() {
        if (!isRegistrationFormValid()) {
            _registrationState.value = RegistrationState.Error("Невалидные данные")
            return
        }

        _registrationState.value = RegistrationState.Loading

        viewModelScope.launch {
            val result = ServerRepository(applicationContext).registrationUser(buildUser())
            _registrationState.value = result

            // Сброс состояния после завершения
            if (result is RegistrationState.Success) {
                resetRegistrationForm()
            }
        }
    }

    // Авторизация пользователя
    fun loginUser() {

        if (!isLoginFormValid()) {
            _loginState.value = LoginState.Error("Невалидные данные")
            return
        }
        viewModelScope.launch {

        _loginState.value = LoginState.Loading


            val result = ServerRepository(applicationContext).loginUser(buildLoginUser())
            _loginState.value = result

            // Сброс состояния после завершения
            if (result is LoginState.Success) {
                resetLoginForm()
            }
        }
    }


    // Сброс формы регистрации
    private fun resetRegistrationForm() {
        _textForRegisterPhoneNumber.value = "+7"
        _textForRegisterLogin.value = ""
        _textForRegisterPassword.value = ""
        _textForRegisterSecondName.value = ""
        _textForRegisterName.value = ""
        _textForRegisterFatherName.value = ""
    }

    // Сброс формы авторизации
    private fun resetLoginForm() {
        _loginTextForPhoneNumber.value = "+7"
        _textForPassword.value = ""
    }
    fun searchUser(){
        viewModelScope.launch {

            _searchState.value = SeacrhState.Loading

            val result = ServerRepository(applicationContext).searchUser(textForSearch.value)
            _searchState.value = result

        }
    }


}