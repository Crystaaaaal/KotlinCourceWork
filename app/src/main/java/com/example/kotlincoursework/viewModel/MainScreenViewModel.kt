package com.example.kotlincoursework.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class MainScreenViewModel : ViewModel() {
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
}