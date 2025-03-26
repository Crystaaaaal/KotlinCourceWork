package com.example.kotlincoursework.API.Repositorys

import android.util.Log
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.ui.theme.state.ServerState
import retrofit2.HttpException
import java.io.IOException

class ServerStatusRepository(
) {
    private val apiService = ApiClient.apiService

    suspend fun checkServerStatus(): ServerState {
        return try {
            Log.d("ServerRepository: checkServerStatus", "Отправление запроса")
            // Устанавливаем состояние Loading перед началом запроса
            ServerState.Loading

            val response = apiService.checkServerStatus()
            Log.d("ServerRepository: checkServerStatus", "Запрос отправлен")

            if (response.isSuccessful) {
                Log.i("ServerRepository: checkServerStatus", "Сервер онлайн!")
                ServerState.Success(true)
            } else {
                ServerState.Error("Ошибка сервера: ${response.code()}")
            }
        } catch (e: IOException) {
            Log.e("ServerRepository: checkServerStatus", "Ошибка сети: ${e.message}")
            Log.d("ServerRepository: checkServerStatus", "Ошибка сети")
            ServerState.Error("Сервер недоступен")
        } catch (e: HttpException) {
            Log.e("ServerRepository: checkServerStatus", "HTTP ошибка: ${e.message}")
            Log.d("ServerRepository: checkServerStatus", "ошибка HTTP")
            ServerState.Error("Сервер недоступен")
        } catch (e: Exception) {
            Log.e("ServerRepository: checkServerStatus", "Неизвестная ошибка: ${e.message}")
            Log.d("ServerRepository: checkServerStatus", "Неизвестная ошибка")
            ServerState.Error("Сервер недоступен")
        }
    }
}

