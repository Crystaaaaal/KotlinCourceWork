package com.example.kotlincoursework.API.Repositorys

import android.util.Log
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.ui.theme.state.GetAllChatState
import com.example.kotlincoursework.ui.theme.state.GetMessagesState
import com.example.kotlincoursework.ui.theme.state.ServerState
import dataBase.ChatRemote
import dataBase.MessagesRecive
import dataBase.MessagesRemote
import dataBase.TokenAndNumberRecive
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class QuitRepository {
    private val apiService = ApiClient.apiService
    private val TAG = "QuitRepository: quit"
    suspend fun quit(user: TokenAndNumberRecive){
        when (val serverState = ServerStatusRepository().checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d(TAG, "Отправление запроса")
                        apiService.Quit(user)
                        Log.d(TAG, "Запрос отправлен")
                    } catch (e: IOException) {
                        Log.e(TAG, "Ошибка сети: ${e.message}")

                    } catch (e: HttpException) {
                        Log.e(TAG, "HTTP ошибка: ${e.message}")

                    } catch (e: Exception) {
                        Log.e(TAG, "Неизвестная ошибка: ${e.message}")

                    }
                } else {
                    Log.d(TAG, "Сервер недоступен")

                }
            }
            is ServerState.Error -> {
                Log.d(TAG, "Ошибка сервера: ${serverState.message}")

            }
            else -> {
                Log.d(TAG, "Состояние сервера не определено")
            }
        }
    }
}