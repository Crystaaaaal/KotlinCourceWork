package com.example.kotlincoursework.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoursework.API.Repositorys.GetAllChatsRepository
import com.example.kotlincoursework.API.Repositorys.LoginRepository
import com.example.kotlincoursework.API.Repositorys.RegistrationRepository
import com.example.kotlincoursework.DB.DAO.ChatDao
import com.example.kotlincoursework.DB.DAO.UserDao
import com.example.kotlincoursework.ui.theme.state.GetAllChatState
import com.example.kotlincoursework.ui.theme.state.GetMessagesState
import com.example.kotlincoursework.ui.theme.state.LoginState
import com.example.kotlincoursework.ui.theme.state.RegistrationState
import dataBase.LoginUser
import dataBase.MessageForShow
import dataBase.RegistrationUserInfo
import dataBase.TokenAndNumberRecive
import dataBase.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin

class AuthenticationViewModel(
    private val sharedPreferences: SharedPreferences,
    private val applicationContext: Context,
    private val db: SQLiteDatabase

): ViewModel() {
    private val userDao by lazy { UserDao(db) }
    private val chatDao by lazy { ChatDao(db) }
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
    private fun isLoginFormValid(): Boolean {
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

    // Состояние регистрации
    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> get() = _registrationState

    // Состояние авторизации
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> get() = _loginState

    private val _getAllChatState = MutableStateFlow<GetAllChatState>(GetAllChatState.Idle)
    val getAllChatState: StateFlow<GetAllChatState> get() = _getAllChatState

    fun resetLoginState() {
        _loginState.value = LoginState.Idle // Сбрасываем состояние
    }

    fun resetGetAllChatState() {
        _getAllChatState.value = GetAllChatState.Idle // Сбрасываем состояние
    }

    fun resetRegistrationState() {
        _registrationState.value = RegistrationState.Idle
    }

    // Регистрация пользователя
    fun registerUser() {
        if (!isRegistrationFormValid()) {
            _registrationState.value = RegistrationState.Error("Невалидные данные")
            return
        }

        _registrationState.value = RegistrationState.Loading

        viewModelScope.launch {
            val result = RegistrationRepository().registrationUser(buildUser())
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


            val result = LoginRepository(applicationContext).loginUser(buildLoginUser())
            _loginState.value = result

            // Сброс состояния после завершения
            if (result is LoginState.Success) {
                resetLoginForm()
            }
        }
    }

    private val _User = MutableStateFlow(
        User(
            phoneNumber = "",
            hashPassword = "",
            fullName = "",
            login = "",
            profileImage = null,
            createdAt = ""
        )
    )
    val User: MutableStateFlow<User> get() = _User


    fun setUser(User: User) {
        _User.update { currentUser ->
            currentUser.copy(
                phoneNumber = User.phoneNumber,
                hashPassword = User.hashPassword,
                fullName = User.fullName,
                login = User.login,
                profileImage = User.profileImage,
                createdAt = User.createdAt
            )
        }
    }

//    fun addUser(){
//        userDao.getOrCreateUser(_User.value)
//    }
//    fun createSeflChat(){
//        chatDao.getOrCreateChat(_User.value.phoneNumber,_User.value.phoneNumber)
//    }

    fun AddChats(){
        viewModelScope.launch {

            _getAllChatState.value = GetAllChatState.Loading
            val tokenAndNumberRecive = createLoginRecive()
            if (tokenAndNumberRecive == null){
                return@launch
            }
            val result =GetAllChatsRepository().getAllChats(tokenAndNumberRecive)
            _getAllChatState.value = result
            handleChatState(_getAllChatState.value)
        }

    }

    private fun handleChatState(state: GetAllChatState) {
        when (state) {
            is GetAllChatState.Success -> {
                val chatDao = getKoin().get<ChatDao>()
                chatDao.clearChatsTable()
                val userDao = getKoin().get<UserDao>()
                userDao.clearUsersTable()
                state.success.Users.forEach { user ->
                    userDao.createUser(user)
                }


                // Добавляем все чаты в базу данных
                state.success.userChatsRecive.forEach { chat ->
                    chatDao.createChat(chat.userPhone,chat.contactPhone)
                }
            }
            GetAllChatState.Loading -> {
                // Можно показать индикатор загрузки
            }
            is GetAllChatState.Error -> {
                // Обработка ошибки, например показать snackbar
            }
            GetAllChatState.Idle -> {
                // Ничего не делаем
            }
        }
    }

    private fun createLoginRecive(): TokenAndNumberRecive? {
        val token = sharedPreferences.getString("auth_token", null)
        val phoneNumber = sharedPreferences.getString("auth_phone", null)
        if (token.isNullOrEmpty() && phoneNumber.isNullOrEmpty()) {
            return null
        }
        return TokenAndNumberRecive(token!!, phoneNumber!!)
    }

}