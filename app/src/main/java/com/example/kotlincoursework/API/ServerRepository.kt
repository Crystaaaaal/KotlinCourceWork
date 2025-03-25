package com.example.kotlincoursework.API

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.kotlincoursework.ui.theme.state.GetUserInfoState
import com.example.kotlincoursework.ui.theme.state.LoginState
import com.example.kotlincoursework.ui.theme.state.RegistrationState
import com.example.kotlincoursework.ui.theme.state.SeacrhState
import com.example.kotlincoursework.ui.theme.state.ServerState
import dataBase.ActiveUser
import dataBase.SearchingResponse
import dataBase.LoginUser
import dataBase.PhoneOrLoginRemote
import dataBase.RegistrationUserInfo
import dataBase.ServerResponse
import dataBase.LoginRecive
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class ServerRepository(
    private val applicationContext: Context
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

    suspend fun loginUser(user: LoginUser): LoginState {
        return when (val serverState = checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d("ServerRepository: loginUser", "Отправление запроса")
                        // Устанавливаем состояние Loading перед началом запроса

                        val response: Response<LoginRecive> = apiService.loginUser(user)
                        Log.d("ServerRepository: loginUser", "Запрос отправлен")

                        if (response.isSuccessful && response.body() != null) {
                            Log.i("ServerRepository: loginUser", "Пользователь успешно авторизован")
                            // Создаем MasterKey для EncryptedSharedPreferences
                            val masterKey = MasterKey.Builder(applicationContext)
                                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                                .build()

                            // Инициализируем EncryptedSharedPreferences
                            val sharedPreferences = EncryptedSharedPreferences.create(
                                applicationContext,
                                "secure_prefs", // Имя файла SharedPreferences
                                masterKey,
                                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                            )
                            // Сохраняем токен
                            sharedPreferences.edit()
                                .putString("auth_token", response.body()!!.token).apply()
                            // Сохраняем номер телефона
                            sharedPreferences.edit()
                                .putString("auth_phone", response.body()!!.phoneNumber).apply()


                            return LoginState.Success(true) // Возвращаем успешный результат
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

    suspend fun searchUser(phoneOrLogin: String, user: LoginRecive): SeacrhState {
        return when (val serverState = checkServerStatus()) {
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

