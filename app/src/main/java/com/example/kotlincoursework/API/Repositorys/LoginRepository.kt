package com.example.kotlincoursework.API.Repositorys

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.ui.theme.state.LoginState
import com.example.kotlincoursework.ui.theme.state.ServerState
import dataBase.LoginRecive
import dataBase.TokenAndNumberRecive
import dataBase.LoginUser
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class LoginRepository(
    private val applicationContext: Context
) {
    private val apiService = ApiClient.apiService

    suspend fun loginUser(user: LoginUser): LoginState {
        return when (val serverState = ServerStatusRepository().checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d("LoginRepository: loginUser", "Отправление запроса")
                        // Устанавливаем состояние Loading перед началом запроса

                        val response: Response<LoginRecive> = apiService.loginUser(user)
                        Log.d("LoginRepository: loginUser", "Запрос отправлен")

                        if (response.isSuccessful && response.body() != null) {
                            Log.i("LoginRepository: loginUser", "Пользователь успешно авторизован")
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
                                .putString("auth_phone", response.body()!!.user.phoneNumber).apply()


                            return LoginState.Success(response.body()!!.user) // Возвращаем успешный результат
                        } else {
                            return when (response.code()) {
                                400 -> {
                                    Log.w("LoginRepository: loginUser", "Некорректный запрос")
                                    LoginState.Error("Некорректный запрос")
                                }

                                401 -> {
                                    Log.w("LoginRepository: loginUser", "Неверные учетные данные")
                                    LoginState.Error("Неверные учетные данные")
                                }

                                409 -> {
                                    Log.w(
                                        "LoginRepository: loginUser",
                                        "Пользователь не зарегистрирован"
                                    )
                                    LoginState.Error("Пользователь не зарегистрирован")
                                }

                                500 -> {
                                    Log.e("LoginRepository: loginUser", "Ошибка сервера")
                                    LoginState.Error("Ошибка сервера")
                                }

                                else -> {
                                    Log.e(
                                        "LoginRepository: loginUser",
                                        "Неизвестная ошибка: ${response.code()}"
                                    )
                                    LoginState.Error("Неизвестная ошибка: ${response.code()}")
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("LoginRepository: loginUser", "Ошибка сети: ${e.message}")
                        return LoginState.Error("Сервер недоступен") // Возвращаем ошибку сети
                    } catch (e: HttpException) {
                        Log.e("LoginRepository: loginUser", "HTTP ошибка: ${e.message}")
                        return LoginState.Error("Сервер недоступен") // Возвращаем HTTP ошибку
                    } catch (e: Exception) {
                        Log.e("LoginRepository: loginUser", "Неизвестная ошибка: ${e.message}")
                        return LoginState.Error("Сервер недоступен") // Возвращаем неизвестную ошибку
                    }
                } else {
                    Log.d("LoginRepository: loginUser", "Сервер недоступен")
                    return LoginState.Error("Сервер недоступен") // Возвращаем ошибку, если сервер оффлайн
                }
            }

            is ServerState.Error -> {
                Log.d("LoginRepository: loginUser", "Ошибка сервера: ${serverState.message}")
                return LoginState.Error(serverState.message) // Возвращаем ошибку сервера
            }

            else -> {
                Log.d("LoginRepository: loginUser", "Состояние сервера не определено")
                return LoginState.Idle // Возвращаем Idle, если сервер не был проверен
            }
        }
    }

}