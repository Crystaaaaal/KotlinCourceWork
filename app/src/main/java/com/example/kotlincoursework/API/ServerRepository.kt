package com.example.kotlincoursework.API

import android.util.Log
import dataBase.RegistrationUserInfo
import dataBase.ServerResponse
import dataBase.Token
import dataBase.User
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ServerRepository {
    private val apiService = ApiClient.apiService

    suspend fun checkServerStatus(): Boolean {
        return try {
            Log.d("ServerRepository: checkServerStatus", "Отправление запроса")
            val response = apiService.checkServerStatus()
            Log.d("ServerRepository: checkServerStatus", "Запрос отправлен")
            if (response.isSuccessful) {
                Log.i("ServerRepository: checkServerStatus", "Сервер онлайн!")
                return true
            } else {
                return false
            }
        } catch (e: IOException) {
            Log.e("ServerRepository: checkServerStatus", "\"Ошибка сети: ${e.message}\"")
            return false
        } catch (e: HttpException) {
            Log.e("ServerRepository: checkServerStatus", "HTTP ошибка: ${e.message}")
            return false
        } catch (e: Exception) {
            Log.e("ServerRepository: checkServerStatus", "Неизвестная ошибка: ${e.message}")
            return false
        }
    }

    suspend fun registrationUser(user: RegistrationUserInfo): Boolean {
        if (checkServerStatus() == false) {
            Log.w("ServerRepository: registrationUser", "Сервер недоступен")
            return false
        }

        return try {
            Log.d("ServerRepository: registrationUser", "Отправление запроса")
            val response: Response<ServerResponse> = apiService.registrationUser(user)
            Log.d("ServerRepository: registrationUser", "Запрос отправлен")

            if (response.isSuccessful && response.body() != null) {
                Log.i("ServerRepository: registrationUser", "Пользователь зарегистрирован")
                return true
            } else {
                // Обработка ошибок
                when (response.code()) {
                    409 -> {
                        Log.w("ServerRepository: registrationUser", "Пользователь уже зарегистрирован")
                    }
                    400 -> {
                        Log.w("ServerRepository: registrationUser", "Некорректный запрос")
                    }
                    500 -> {
                        Log.e("ServerRepository: registrationUser", "Ошибка сервера")
                    }
                    else -> {
                        Log.e("ServerRepository: registrationUser", "Неизвестная ошибка: ${response.code()}")
                    }
                }
                return false
            }
        } catch (e: IOException) {
            Log.e("ServerRepository: registrationUser", "Ошибка сети: ${e.message}")
            return false
        } catch (e: HttpException) {
            Log.e("ServerRepository: registrationUser", "HTTP ошибка: ${e.message}")
            return false
        } catch (e: Exception) {
            Log.e("ServerRepository: registrationUser", "Неизвестная ошибка: ${e.message}")
            return false
        }
    }
}
