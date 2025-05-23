
package com.example.kotlincoursework.API.Repositorys

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.ui.theme.state.GetMessagesState
import com.example.kotlincoursework.ui.theme.state.LoginState
import com.example.kotlincoursework.ui.theme.state.ServerState
import dataBase.LoginRecive
import dataBase.LoginUser
import dataBase.MessagesRecive
import dataBase.MessagesRemote
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MessagesRepository {
    private val apiService = ApiClient.apiService

    suspend fun getMessages(user: MessagesRecive): GetMessagesState {
        return when (val serverState = ServerStatusRepository().checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d("MessagesRepository: getMessages", "Отправление запроса")
                        // Устанавливаем состояние Loading перед началом запроса

                        val response: Response<MessagesRemote> = apiService.getMessages(user)
                        Log.d("MessagesRepository: getMessages", "Запрос отправлен")

                        if (response.isSuccessful && response.body() != null) {
                            Log.i("MessagesRepository: getMessages", "Сообщения получены")
                            return GetMessagesState.Success(response.body()!!) // Возвращаем успешный результат
                        } else {
                            return when (response.code()) {
                                400 -> {
                                    Log.w("MessagesRepository: getMessages", "Некорректный запрос")
                                    GetMessagesState.Error("Некорректный запрос")
                                }

                                401 -> {
                                    Log.w("MessagesRepository: getMessages", "Неверные учетные данные")
                                    GetMessagesState.Error("Неверные учетные данные")
                                }

                                500 -> {
                                    Log.e("MessagesRepository: getMessages", "Ошибка сервера")
                                    GetMessagesState.Error("Ошибка сервера")
                                }

                                else -> {
                                    Log.e(
                                        "MessagesRepository: getMessages",
                                        "Неизвестная ошибка: ${response.code()}"
                                    )
                                    GetMessagesState.Error("Неизвестная ошибка: ${response.code()}")
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("MessagesRepository: getMessages", "Ошибка сети: ${e.message}")
                        return GetMessagesState.Error("Сервер недоступен") // Возвращаем ошибку сети
                    } catch (e: HttpException) {
                        Log.e("MessagesRepository: getMessages", "HTTP ошибка: ${e.message}")
                        return GetMessagesState.Error("Сервер недоступен") // Возвращаем HTTP ошибку
                    } catch (e: Exception) {
                        Log.e("MessagesRepository: getMessages", "Неизвестная ошибка: ${e.message}")
                        return GetMessagesState.Error("Сервер недоступен") // Возвращаем неизвестную ошибку
                    }
                } else {
                    Log.d("MessagesRepository: getMessages", "Сервер недоступен")
                    return GetMessagesState.Error("Сервер недоступен") // Возвращаем ошибку, если сервер оффлайн
                }
            }

            is ServerState.Error -> {
                Log.d("MessagesRepository: getMessages", "Ошибка сервера: ${serverState.message}")
                return GetMessagesState.Error(serverState.message) // Возвращаем ошибку сервера
            }

            else -> {
                Log.d("MessagesRepository: getMessages", "Состояние сервера не определено")
                return GetMessagesState.Idle // Возвращаем Idle, если сервер не был проверен
            }
        }
    }
}
