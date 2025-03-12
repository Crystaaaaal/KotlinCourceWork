package com.example.kotlincoursework.API

import android.util.Log
import com.example.kotlincoursework.ui.theme.state.LoginState
import com.example.kotlincoursework.ui.theme.state.RegistrationState
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import com.example.kotlincoursework.ui.theme.state.ServerState
import dataBase.ChatingResponse
import dataBase.LoginUser
import dataBase.PhoneOrLoginRemote
import dataBase.RegistrationUserInfo
import dataBase.ServerResponse
import dataBase.Token
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ServerRepository {
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
            Log.d("ServerRepository: checkServerStatus", "ошибка 3")
            ServerState.Error("Сервер недоступен")
        } catch (e: HttpException) {
            Log.e("ServerRepository: checkServerStatus", "HTTP ошибка: ${e.message}")
            Log.d("ServerRepository: checkServerStatus", "ошибка 2")
            ServerState.Error("Сервер недоступен")
        } catch (e: Exception) {
            Log.e("ServerRepository: checkServerStatus", "Неизвестная ошибка: ${e.message}")
            Log.d("ServerRepository: checkServerStatus", "ошибка 1")
            ServerState.Error("Сервер недоступен")
        }
    }

    suspend fun registrationUser(user: RegistrationUserInfo): RegistrationState {
        return when (val serverState = checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d("ServerRepository: registrationUser", "Отправление запроса")
                        // Устанавливаем состояние Loading перед началом запроса
                        RegistrationState.Loading

                        val response: Response<ServerResponse> = apiService.registrationUser(user)
                        Log.d("ServerRepository: registrationUser", "Запрос отправлен")

                        if (response.isSuccessful && response.body() != null) {
                            Log.i("ServerRepository: registrationUser", "Пользователь зарегистрирован")
                            return RegistrationState.Success(true) // Возвращаем успешный результат
                        } else {
                            return when (response.code()) {
                                409 -> {
                                    Log.w("ServerRepository: registrationUser", "Пользователь уже зарегистрирован")
                                    RegistrationState.Error("Пользователь уже зарегистрирован")
                                }

                                400 -> {
                                    Log.w("ServerRepository: registrationUser", "Некорректный запрос")
                                    RegistrationState.Error("Некорректный запрос")
                                }

                                500 -> {
                                    Log.e("ServerRepository: registrationUser", "Ошибка сервера")
                                    RegistrationState.Error("Ошибка сервера")
                                }

                                else -> {
                                    Log.e("ServerRepository: registrationUser", "Неизвестная ошибка: ${response.code()}")
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
                        Log.e("ServerRepository: registrationUser", "Неизвестная ошибка: ${e.message}")
                        return RegistrationState.Error("Неизвестная ошибка: ${e.message}") // Возвращаем неизвестную ошибку
                    }
                } else {
                    Log.d("ServerRepository: registrationUser", "Сервер недоступен")
                    return RegistrationState.Error("Сервер недоступен") // Возвращаем ошибку, если сервер оффлайн
                }
            }

            is ServerState.Error -> {
                Log.d("ServerRepository: registrationUser", "Ошибка сервера: ${serverState.message}")
                return RegistrationState.Error(serverState.message) // Возвращаем ошибку сервера
            }

            else -> {
                Log.d("ServerRepository: registrationUser", "Состояние сервера не определено")
                return RegistrationState.Idle // Возвращаем Idle, если сервер не был проверен
            }
        }
    }

    suspend fun loginUser(user: LoginUser): LoginState {
        return when (val serverState = checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d("ServerRepository: loginUser", "Отправление запроса")
                        // Устанавливаем состояние Loading перед началом запроса
                        LoginState.Loading

                        val response: Response<Token> = apiService.loginUser(user)
                        Log.d("ServerRepository: loginUser", "Запрос отправлен")

                        if (response.isSuccessful && response.body() != null) {
                            Log.i("ServerRepository: loginUser", "Пользователь успешно авторизован")
                            return LoginState.Success(response.body()!!) // Возвращаем успешный результат
                        } else {
                            return when (response.code()) {
                                400 -> {
                                    Log.w("ServerRepository: loginUser", "Некорректный запрос")
                                    LoginState.Error("Некорректный запрос")
                                }

                                401 -> {
                                    Log.w("ServerRepository: loginUser", "Неверные учетные данные")
                                    LoginState.Error("Неверные учетные данные")
                                }

                                409 -> {
                                    Log.w(
                                        "ServerRepository: loginUser",
                                        "Пользователь не зарегистрирован"
                                    )
                                    LoginState.Error("Пользователь не зарегистрирован")
                                }

                                500 -> {
                                    Log.e("ServerRepository: loginUser", "Ошибка сервера")
                                    LoginState.Error("Ошибка сервера")
                                }

                                else -> {
                                    Log.e(
                                        "ServerRepository: loginUser",
                                        "Неизвестная ошибка: ${response.code()}"
                                    )
                                    LoginState.Error("Неизвестная ошибка: ${response.code()}")
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("ServerRepository: loginUser", "Ошибка сети: ${e.message}")
                        return LoginState.Error("Сервер недоступен") // Возвращаем ошибку сети
                    } catch (e: HttpException) {
                        Log.e("ServerRepository: loginUser", "HTTP ошибка: ${e.message}")
                        return LoginState.Error("Сервер недоступен") // Возвращаем HTTP ошибку
                    } catch (e: Exception) {
                        Log.e("ServerRepository: loginUser", "Неизвестная ошибка: ${e.message}")
                        return LoginState.Error("Сервер недоступен") // Возвращаем неизвестную ошибку
                    }
                } else {
                    Log.d("ServerRepository: loginUser", "Сервер недоступен")
                    return LoginState.Error("Сервер недоступен") // Возвращаем ошибку, если сервер оффлайн
                }
            }

            is ServerState.Error -> {
                Log.d("ServerRepository: loginUser", "Ошибка сервера: ${serverState.message}")
                return LoginState.Error(serverState.message) // Возвращаем ошибку сервера
            }

            else -> {
                Log.d("ServerRepository: loginUser", "Состояние сервера не определено")
                return LoginState.Idle // Возвращаем Idle, если сервер не был проверен
            }
        }
    }


    suspend fun searchUser(phoneOrLogin: String): SeacrhState {
        return when (val serverState = checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        SeacrhState.Loading
                        val response: Response<ChatingResponse> = apiService.searchUser(
                            phoneOnLogin = PhoneOrLoginRemote(phoneOrLogin)
                        )
                        if (response.isSuccessful) {
                            SeacrhState.Success(response.body()!!.userList)
                        } else {
                            when (response.code()) {
                                400 -> {
                                    Log.w("ServerRepository: searchUser", "Некорректный запрос")
                                    SeacrhState.Error("Некорректный запрос")
                                }

                                401 -> {
                                    Log.w("ServerRepository: searchUser", "Неверные учетные данные")
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
                    } catch (e: HttpException) {
                        Log.e("ServerRepository: searchUser", "HTTP ошибка: ${e.message}")
                    } catch (e: Exception) {
                        Log.e("ServerRepository: searchUser", "Неизвестная ошибка: ${e.message}")
                    }
                    SeacrhState.Error("Сервер недоступен")
                } else {
                    SeacrhState.Error("Сервер недоступен")
                }
            }

            is ServerState.Error -> SeacrhState.Error(serverState.message)
            else -> SeacrhState.Idle // Возвращаем Idle, если сервер не был проверен
        }
    }
}