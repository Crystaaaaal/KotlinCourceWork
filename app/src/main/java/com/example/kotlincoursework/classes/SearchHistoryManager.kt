package com.example.kotlincoursework.classes

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import dataBase.User
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class SearchHistoryManager(context: Context) {
    private val sharedPref = context.getSharedPreferences("USER_HISTORY", Context.MODE_PRIVATE)
    private val json = Json { ignoreUnknownKeys = true }
    private val maxHistorySize = 5

    fun saveUser(user: User) {
        val history = getUserHistory().toMutableList()
        history.removeAll { it.phoneNumber == user.phoneNumber && it.login == user.login }
        history.add(0, user)
        if (history.size > maxHistorySize) {
            history.subList(maxHistorySize, history.size).clear()
        }
        sharedPref.edit().putString("user_history", json.encodeToString(history)).apply()
    }

    fun getUserHistory(): List<User> {
        val jsonString = sharedPref.getString("user_history", null)
        return if (jsonString.isNullOrEmpty()) {
            emptyList()
        } else {
            try {
                json.decodeFromString<List<User>>(jsonString)
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    fun clearUserHistory() {
        sharedPref.edit().remove("user_history").apply()
    }
}

@Composable
fun rememberUserHistoryManager(): State<SearchHistoryManager> {
    val context = LocalContext.current
    return remember {
        mutableStateOf(SearchHistoryManager(context))
    }
}