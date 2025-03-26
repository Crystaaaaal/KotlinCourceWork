package com.example.kotlincoursework.viewModel

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class viewModel(
    private val applicationContext: Context,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

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