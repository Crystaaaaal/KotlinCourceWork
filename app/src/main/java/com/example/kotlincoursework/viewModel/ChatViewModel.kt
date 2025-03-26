package com.example.kotlincoursework.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlincoursework.API.Repositorys.SearchRepository
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import dataBase.LoginRecive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val applicationContext: Context,
    private val sharedPreferences: SharedPreferences
):ViewModel() {

    //Для текста поиска в чатах
    private val _textForSearch = MutableStateFlow("")
    val textForSearch: StateFlow<String> get() = _textForSearch

    //Обновление текста поиска
    fun updateTextForSearch(text: String) {
        _textForSearch.value = text
    }

    // Состояние поиска
    private val _searchState = MutableStateFlow<SeacrhState>(SeacrhState.Idle)
    val searchState: StateFlow<SeacrhState> get() = _searchState

    fun resetSearchState() {
        _searchState.value = SeacrhState.Idle
    }

    fun searchUser() {
        viewModelScope.launch {
            _searchState.value = SeacrhState.Loading

            val loginRecive = createLoginRecive()
            if (loginRecive == null) {
                //TODO выкидывание из ака и переход на экран входа
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
        val loginRecive = LoginRecive(token!!, phoneNumber!!)
        return loginRecive
    }

}