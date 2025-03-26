package com.example.kotlincoursework.API.Repositorys

import android.util.Log
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import com.example.kotlincoursework.ui.theme.state.ServerState
import dataBase.LoginRecive
import dataBase.PhoneOrLoginRemote
import dataBase.SearchingResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class SearchRepository() {
    private val apiService = ApiClient.apiService

    suspend fun searchUser(phoneOrLogin: String, user: LoginRecive): SeacrhState {
        return when (val serverState = ServerStatusRepository().checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        val response: Response<SearchingResponse> = apiService.searchUser(
                            phoneOnLogin = PhoneOrLoginRemote(
                                phoneOrLogin = phoneOrLogin,
                                token = user
                            )
                        )
                        if (response.isSuccessful && response.body() != null) {
                            return SeacrhState.Success(response.body()!!.userList) // Возвращаем успешный результат
                        } else {
                            return when (response.code()) {
                                400 -> {
                                    Log.w("ServerRepository: searchUser", "Некорректный запрос")
                                    SeacrhState.Error("Некорректный запрос")
                                }

                                401 -> {
                                    Log.w(
                                        "ServerRepository: searchUser",
                                        "Неверные учетные данные"
                                    )
                                    SeacrhState.Error("Неверные учетные данные")
                                }

                                409 -> {
                                    Log.w(
                                        "ServerRepository: searchUser",
                                        "Пользователь не зарегистрирован"
                                    )
                                    SeacrhState.Error("Пользователь не зарегистрирован")
                                }

                                500 -> {
                                    Log.e("ServerRepository: searchUser", "Ошибка сервера")
                                    SeacrhState.Error("Ошибка сервера")
                                }

                                else -> {
                                    Log.e(
                                        "ServerRepository: searchUser",
                                        "Неизвестная ошибка: ${response.code()}"
                                    )
                                    SeacrhState.Error("Неизвестная ошибка: ${response.code()}")
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("ServerRepository: searchUser", "Ошибка сети: ${e.message}")
                        return SeacrhState.Error("Ошибка сети: ${e.message}") // Возвращаем ошибку сети
                    } catch (e: HttpException) {
                        Log.e("ServerRepository: searchUser", "HTTP ошибка: ${e.message}")
                        return SeacrhState.Error("HTTP ошибка: ${e.message}") // Возвращаем HTTP ошибку
                    } catch (e: Exception) {
                        Log.e(
                            "ServerRepository: searchUser",
                            "Неизвестная ошибка: ${e.message}"
                        )
                        return SeacrhState.Error("Неизвестная ошибка: ${e.message}") // Возвращаем неизвестную ошибку
                    }
                } else {
                    Log.d("ServerRepository: searchUser", "Сервер недоступен")
                    return SeacrhState.Error("Сервер недоступен") // Возвращаем ошибку, если сервер оффлайн
                }
            }

            is ServerState.Error -> {
                Log.d("ServerRepository: searchUser", "Ошибка сервера: ${serverState.message}")
                return SeacrhState.Error(serverState.message) // Возвращаем ошибку сервера
            }

            else -> {
                Log.d("ServerRepository: searchUser", "Состояние сервера не определено")
                return SeacrhState.Idle // Возвращаем Idle, если сервер не был проверен
            }
        }
    }
}