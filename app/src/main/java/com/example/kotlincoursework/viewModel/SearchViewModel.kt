package com.example.kotlincoursework.viewModel

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoursework.API.Repositorys.SearchRepository
import com.example.kotlincoursework.DB.DAO.ChatDao
import com.example.kotlincoursework.DB.DAO.UserDao
import com.example.kotlincoursework.classes.SearchHistoryManager
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import dataBase.MessageForShow
import dataBase.TokenAndNumberRecive
import dataBase.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val applicationContext: Context,
    private val sharedPreferences: SharedPreferences,
    private val db: SQLiteDatabase
): ViewModel() {
    private val userDao by lazy { UserDao(db) }
    private val chatDao by lazy { ChatDao(db) }


    private val _textForSearch = MutableStateFlow("")
    val textForSearch: StateFlow<String> get() = _textForSearch

    private val _searchState = MutableStateFlow<SeacrhState>(SeacrhState.Idle)
    val searchState: StateFlow<SeacrhState> get() = _searchState

    private val _searchHistory: SnapshotStateList<User> = mutableStateListOf()
    val searchHistory: List<User> get() = _searchHistory

    private val _historyUpdated = mutableStateOf(false)
    val historyUpdated get() = _historyUpdated

    fun updateTextForSearch(text: String) {
        _textForSearch.value = text
        if (text.length > 0) {
            searchUser()
        } else {
            resetSearchState()
        }
    }

    fun resetSearchText(){
        _textForSearch.value = ""
    }

    fun resetSearchState() {
        _searchState.value = SeacrhState.Idle
    }

    fun searchUser() {
        viewModelScope.launch {
            _searchState.value = SeacrhState.Loading
            val loginRecive = createLoginRecive()
            if (loginRecive == null) {
                return@launch
            }

            val result = SearchRepository().searchUser(
                textForSearch.value,
                loginRecive!!
            )
            _searchState.value = result
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

    fun loadHistory(manager: SearchHistoryManager) {
        _searchHistory.clear()
        _searchHistory.addAll(manager.getUserHistory())
        _historyUpdated.value = !_historyUpdated.value
    }

    fun clearHistory(manager: SearchHistoryManager) {
        manager.clearUserHistory()
        loadHistory(manager)
    }

    private val _allChatUser = mutableStateListOf<User>()
    val allChatUser: List<User> get() = _allChatUser

    fun getAllUsers(){
        _allChatUser.clear()
        val usersList = chatDao.getContactsFromChats()
        _allChatUser.addAll(usersList)

    }
}