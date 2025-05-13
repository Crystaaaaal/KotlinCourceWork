package com.example.kotlincoursework.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.API.Repositorys.SearchRepository
import com.example.kotlincoursework.DB.DAO.ChatDao
import com.example.kotlincoursework.DB.DAO.UserDao
import com.example.kotlincoursework.DB.DatabaseHelper
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import dataBase.TokenAndNumberRecive
import dataBase.MessageForShow
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
}