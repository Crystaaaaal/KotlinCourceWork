package com.example.kotlincoursework.API.Repositorys

import android.util.Log
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.ui.theme.state.RegistrationState
import com.example.kotlincoursework.ui.theme.state.ServerState
import dataBase.RegistrationUserInfo
import dataBase.ServerResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class RegistrationRepository(
) {
    private val apiService = ApiClient.apiService

    suspend fun registrationUser(user: RegistrationUserInfo): RegistrationState {
        return when (val serverState = ServerStatusRepository().checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d("ServerRepository: registrationUser", "Отправление запроса")
                        // Устанавливаем состояние Loading перед началом запроса
                        RegistrationState.Loading

                        val response: Response<ServerResponse> = apiService.registrationUser(user)
                        Log.d("ServerRepository: registrationUser", "Запрос отправлен")

                        if (response.isSuccessful && response.body() != null) {
                            Log.i(
                                "ServerRepository: registrationUser",
                                "Пользователь зарегистрирован"
                            )
                            return RegistrationState.Success(true) // Возвращаем успешный результат
                        } else {
                            return when (response.code()) {
                                409 -> {
                                    Log.w(
                                        "ServerRepository: registrationUser",
                                        "Пользователь уже зарегистрирован"
                                    )
                                    RegistrationState.Error("Пользователь уже зарегистрирован")
                                }

                                400 -> {
                                    Log.w(
                                        "ServerRepository: registrationUser",
                                        "Некорректный запрос"
                                    )
                                    RegistrationState.Error("Некорректный запрос")
                                }

                                500 -> {
                                    Log.e("ServerRepository: registrationUser", "Ошибка сервера")
                                    RegistrationState.Error("Ошибка сервера")
                                }

                                else -> {
                                    Log.e(
                                        "ServerRepository: registrationUser",
                                        "Неизвестная ошибка: ${response.code()}"
                                    )
                                    RegistrationState.Error("Неизвестная ошибка: ${response.code()}")
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("ServerRepository: registrationUser", "Ошибка сети: ${e.message}")
                        return RegistrationState.Error("Ошибка сети: ${e.message}") // Возвращаем ошибку сети
                    } catch (e: HttpException) {
                        Log.e("ServerRepository: registrationUser", "HTTP ошибка: ${e.message}")
                        return RegistrationState.Error("HTTP ошибка: ${e.message}") // Возвращаем HTTP ошибку
                    } catch (e: Exception) {
                        Log.e(
                            "ServerRepository: registrationUser",
                            "Неизвестная ошибка: ${e.message}"
                        )
                        return RegistrationState.Error("Неизвестная ошибка: ${e.message}") // Возвращаем неизвестную ошибку
                    }
                } else {
                    Log.d("ServerRepository: registrationUser", "Сервер недоступен")
                    return RegistrationState.Error("Сервер недоступен") // Возвращаем ошибку, если сервер оффлайн
                }
            }

            is ServerState.Error -> {
                Log.d(
                    "ServerRepository: registrationUser",
                    "Ошибка сервера: ${serverState.message}"
                )
                return RegistrationState.Error(serverState.message) // Возвращаем ошибку сервера
            }

            else -> {
                Log.d("ServerRepository: registrationUser", "Состояние сервера не определено")
                return RegistrationState.Idle // Возвращаем Idle, если сервер не был проверен
            }
        }
    }



}