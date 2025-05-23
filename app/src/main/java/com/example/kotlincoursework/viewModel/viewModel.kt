package com.example.kotlincoursework.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.API.Repositorys.MessagesRepository
import com.example.kotlincoursework.API.Repositorys.SearchRepository
import com.example.kotlincoursework.DB.DAO.ChatDao
import com.example.kotlincoursework.DB.DAO.UserDao
import com.example.kotlincoursework.DB.DatabaseHelper
import com.example.kotlincoursework.ui.theme.state.GetMessagesState
import com.example.kotlincoursework.ui.theme.state.GetUserInfoState
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import dataBase.ChatRecive
import dataBase.TokenAndNumberRecive
import dataBase.MessageForShow
import dataBase.MessagesRecive
import dataBase.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class viewModel(
    private val applicationContext: Context,
    private val sharedPreferences: SharedPreferences,
    private val db: SQLiteDatabase,
    private val dbHelper: DatabaseHelper,

) : ViewModel() {
    private val userDao by lazy { UserDao(db) }
    private val chatDao by lazy { ChatDao(db) }

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


    fun setUser(chatUser: User) {
        _User.update { currentUser ->
            currentUser.copy(
                phoneNumber = chatUser.phoneNumber,
                hashPassword = chatUser.hashPassword,
                fullName = chatUser.fullName,
                login = chatUser.login,
                profileImage = chatUser.profileImage,
                createdAt = chatUser.createdAt
            )
        }
    }

    fun buildAndSendMessage(messageText: String, sentAt: String) {
        viewModelScope.launch {
            val loginRecive = createLoginRecive()
            if (loginRecive == null) {
                return@launch
            }
            ApiClient.sendMessage(
                forUser = User.value,
                fromUser = loginRecive,
                text = messageText,
                sentAt = sentAt
            )

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


    var topBarText by mutableStateOf("Мессенджер")
        private set

    private val _items = mutableStateListOf<MessageForShow>()
    val items: List<MessageForShow> get() = _items

    private val _incomingItems = mutableStateListOf<MessageForShow>()
    val incomingItems: List<MessageForShow> get() = _incomingItems

    fun clearMessagesList(){
        _items.clear()
        _incomingItems.clear()
    }


    fun createChatOrUser(user: User){
            userDao.getOrCreateUser(user)
            val phoneNumber = sharedPreferences.getString("auth_phone", null)
            if (!phoneNumber.isNullOrEmpty()) {
                chatDao.getOrCreateChat(phoneNumber,user.phoneNumber)
            }
    }

    override fun onCleared() {
        dbHelper.close()
        super.onCleared()
    }


    fun addIcomingItem(item: MessageForShow) {
        _incomingItems.add(item)
    }

    fun addItem(item: MessageForShow) {
        _items.add(item)
    }

    fun updateTopBarText(newText: String) {
        topBarText = newText
    }

    // Состояние получения данных пользователя
    private val _getMessagesState = MutableStateFlow<GetMessagesState>(GetMessagesState.Idle)
    val getMessagesState: StateFlow<GetMessagesState> get() = _getMessagesState

    private val TAG = "MessageViewModel"

    fun getMessages() {
        Log.d(TAG, "Вызов getMessages()")
        viewModelScope.launch {
            Log.d(TAG, "Начало получения сообщений...")
            _getMessagesState.value = GetMessagesState.Loading
            Log.d(TAG, "Установка состояния: Loading")

            val messagesRecive = createMessageRecive()
            if (messagesRecive == null) {
                Log.e(TAG, "Ошибка: createMessageRecive() вернул null")
                _getMessagesState.value = GetMessagesState.Error("Ошибка")
                return@launch
            }
            Log.d(TAG, "Успешно создан MessagesRecive")

            try {
                val result = MessagesRepository().getMessages(messagesRecive)
                Log.d(TAG, "Успешный запрос к репозиторию, получен результат типа: ${result.javaClass.simpleName}")
                _getMessagesState.value = result
                Log.d(TAG, "Состояние обновлено результатом")
            } catch (e: Exception) {
                Log.e(TAG, "Ошибка при запросе к репозиторию: ${e.message}", e)
                _getMessagesState.value = GetMessagesState.Error(e.message ?: "Неизвестная ошибка")
            }
        }
    }

    fun createMessageRecive(): MessagesRecive? {
        Log.d(TAG, "Вызов createMessageRecive()")

        val loginRecive = createLoginRecive()
        if (loginRecive == null) {
            Log.e(TAG, "Ошибка: createLoginRecive() вернул null")
            return null
        }
        Log.d(TAG, "Успешно получен loginRecive")

        val chat = getChat()
        if (chat == null) {
            Log.e(TAG, "Ошибка: getChat() вернул null")
            return null
        }
        Log.d(TAG, "Успешно получен чат")

        val messagesRecive = MessagesRecive(Chat = chat, token = loginRecive)
        Log.d(TAG, "Создан MessagesRecive")
        return messagesRecive
    }

    fun getChat(): ChatRecive? {
        Log.d(TAG, "Вызов getChat()")

        val phoneNumber = sharedPreferences.getString("auth_phone", null)
        if (phoneNumber.isNullOrEmpty()) {
            Log.e(TAG, "Ошибка: В SharedPreferences не найден номер телефона")
            return null
        }
        Log.d(TAG, "Найден номер телефона в SharedPreferences")

        val chat = chatDao.getChatByPhoneNumbers(phoneNumber!!, _User.value.phoneNumber)
        if (chat == null) {
            Log.e(TAG, "Ошибка: Не найден чат для номеров: ${phoneNumber.take(3)}... и ${_User.value.phoneNumber.take(3)}...")
            return null
        }
        Log.d(TAG, "Найден чат в базе данных")

        val chatRecive = ChatRecive(
            chatId = chat.chatId.toInt(),
            userPhone = chat.userPhone,
            contactPhone = chat.contactPhone,
            createdAt = chat.createdAt.toString()
        )
        Log.d(TAG, "Создан ChatRecive")
        return chatRecive
    }

     fun fillItems(){
         handleMessagesState(_getMessagesState.value)
    }

    fun resetGetMessagesState() {
        _getMessagesState.value = GetMessagesState.Idle
    }

    private fun handleMessagesState(state: GetMessagesState) {
        when (state) {
            is GetMessagesState.Success -> {
                _items.clear()
                _incomingItems.clear()

                // Преобразуем MessageIncoming в MessageForShow и добавляем в соответствующие списки
                state.success.userMessagesRecive.mapTo(_items) { message ->
                    MessageForShow(
                        messageText = message.messageText,
                        sentAt = message.sentAt
                    )
                }

                state.success.contactMessage.mapTo(_incomingItems) { message ->
                    MessageForShow(
                        messageText = message.messageText,
                        sentAt = message.sentAt
                    )
                }

                // Если нужно объединить списки в один (опционально)
                // val allMessages = _items + _incomingItems
                // _items.clear()
                // _items.addAll(allMessages.sortedBy { it.timestamp })
            }
            GetMessagesState.Loading -> {
                // Можно показать индикатор загрузки
            }
            is GetMessagesState.Error -> {
                // Обработка ошибки, например показать snackbar
            }
            GetMessagesState.Idle -> {
                // Ничего не делаем
            }
        }
    }

}