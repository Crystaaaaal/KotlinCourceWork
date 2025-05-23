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

class GetAllChatsRepository {
    private val apiService = ApiClient.apiService
    private val TAG = "GetAllChatsRepository: getAllChats"
    suspend fun getAllChats(user: TokenAndNumberRecive): GetAllChatState {
        return when (val serverState = ServerStatusRepository().checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d(TAG, "Отправление запроса")
                        // Устанавливаем состояние Loading перед началом запроса

                        val response: Response<ChatRemote> = apiService.getAllChats(user)
                        Log.d(TAG, "Запрос отправлен")

                        if (response.isSuccessful && response.body() != null) {
                            Log.i(TAG, "Чаты получены")
                            return GetAllChatState.Success(response.body()!!) // Возвращаем успешный результат
                        } else {
                            return when (response.code()) {
                                400 -> {
                                    Log.w(TAG, "Некорректный запрос")
                                    GetAllChatState.Error("Некорректный запрос")
                                }

                                401 -> {
                                    Log.w(TAG, "Неверные учетные данные")
                                    GetAllChatState.Error("Неверные учетные данные")
                                }

                                500 -> {
                                    Log.e(TAG, "Ошибка сервера")
                                    GetAllChatState.Error("Ошибка сервера")
                                }

                                else -> {
                                    Log.e(
                                        TAG,
                                        "Неизвестная ошибка: ${response.code()}"
                                    )
                                    GetAllChatState.Error("Неизвестная ошибка: ${response.code()}")
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.e(TAG, "Ошибка сети: ${e.message}")
                        return GetAllChatState.Error("Сервер недоступен") // Возвращаем ошибку сети
                    } catch (e: HttpException) {
                        Log.e(TAG, "HTTP ошибка: ${e.message}")
                        return GetAllChatState.Error("Сервер недоступен") // Возвращаем HTTP ошибку
                    } catch (e: Exception) {
                        Log.e(TAG, "Неизвестная ошибка: ${e.message}")
                        return GetAllChatState.Error("Сервер недоступен") // Возвращаем неизвестную ошибку
                    }
                } else {
                    Log.d(TAG, "Сервер недоступен")
                    return GetAllChatState.Error("Сервер недоступен") // Возвращаем ошибку, если сервер оффлайн
                }
            }

            is ServerState.Error -> {
                Log.d(TAG, "Ошибка сервера: ${serverState.message}")
                return GetAllChatState.Error(serverState.message) // Возвращаем ошибку сервера
            }

            else -> {
                Log.d(TAG, "Состояние сервера не определено")
                return GetAllChatState.Idle // Возвращаем Idle, если сервер не был проверен
            }
        }
    }
}