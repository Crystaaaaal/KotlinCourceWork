package com.example.kotlincoursework

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


class MainScreenViewModel : ViewModel() {
    var topBarText by mutableStateOf("Мессенджер")
        private set

    fun updateTopBarText(newText: String) {
        topBarText = newText
    }
}