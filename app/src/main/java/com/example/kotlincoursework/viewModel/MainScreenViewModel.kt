package com.example.kotlincoursework.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel


class MainScreenViewModel : ViewModel() {
    var topBarText by mutableStateOf("Мессенджер")
        private set

    fun updateTopBarText(newText: String) {
        topBarText = newText
    }
}