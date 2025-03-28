package com.example.kotlincoursework.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoursework.API.Repositorys.SearchRepository
import com.example.kotlincoursework.classes.SearchHistoryManager
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import dataBase.LoginRecive
import dataBase.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val applicationContext: Context,
    private val sharedPreferences: SharedPreferences,
): ViewModel() {

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

    private fun createLoginRecive(): LoginRecive? {
        val token = sharedPreferences.getString("auth_token", null)
        val phoneNumber = sharedPreferences.getString("auth_phone", null)
        if (token.isNullOrEmpty() && phoneNumber.isNullOrEmpty()) {
            return null
        }
        return LoginRecive(token!!, phoneNumber!!)
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
}