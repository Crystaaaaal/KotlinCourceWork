package com.example.kotlincoursework.API

import android.content.Context
import android.util.Log
import com.example.kotlincoursework.ui.theme.state.GetUserInfoState
import com.example.kotlincoursework.ui.theme.state.ServerState
import com.example.kotlincoursework.ui.theme.state.UpdateUserInfoState
import dataBase.ActiveUser
import dataBase.LoginRecive
import dataBase.ServerResponse
import dataBase.UpdateUser
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class UserInfoRepository(
    private val applicationContext: Context
) {
    private val apiService = ApiClient.apiService

    suspend fun getUserInfo(user: LoginRecive): GetUserInfoState {
        return when (val serverState = ServerRepository(applicationContext).checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d("ServerRepository: getUserInfo", "Отправление запроса")

                        val response: Response<ActiveUser> = apiService.getUserInfo(user)
                        Log.d("ServerRepository: getUserInfo", "Запрос отправлен")

                        if (response.isSuccessful && response.body() != null) {
                            Log.i("ServerRepository: getUserInfo", "Данные получены")

                            return GetUserInfoState.Success(response.body()!!) // Возвращаем успешный результат
                        } else {
                            return when (response.code()) {
                                400 -> {
                                    Log.w("ServerRepository: getUserInfo", "Некорректный запрос")
                                    GetUserInfoState.Error("Некорректный запрос")
                                }

                                409 -> {
                                    Log.w(
                                        "ServerRepository: getUserInfo",
                                        "Неверный токен"
                                    )
                                    GetUserInfoState.Error("Неверный токен")
                                }

                                500 -> {
                                    Log.e("ServerRepository: getUserInfo", "Ошибка сервера")
                                    GetUserInfoState.Error("Ошибка сервера")
                                }

                                else -> {
                                    Log.e(
                                        "ServerRepository: getUserInfo",
                                        "Неизвестная ошибка: ${response.code()}"
                                    )
                                    GetUserInfoState.Error("Неизвестная ошибка: ${response.code()}")
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("ServerRepository: getUserInfo", "Ошибка сети: ${e.message}")
                        return GetUserInfoState.Error("Сервер недоступен") // Возвращаем ошибку сети
                    } catch (e: HttpException) {
                        Log.e("ServerRepository: getUserInfo", "HTTP ошибка: ${e.message}")
                        return GetUserInfoState.Error("Сервер недоступен") // Возвращаем HTTP ошибку
                    } catch (e: Exception) {
                        Log.e("ServerRepository: getUserInfo", "Неизвестная ошибка: ${e.message}")
                        return GetUserInfoState.Error("Сервер недоступен") // Возвращаем неизвестную ошибку
                    }
                } else {
                    Log.d("ServerRepository: getUserInfo", "Сервер недоступен")
                    return GetUserInfoState.Error("Сервер недоступен") // Возвращаем ошибку, если сервер оффлайн
                }
            }

            is ServerState.Error -> {
                Log.d("ServerRepository: getUserInfo", "Ошибка сервера: ${serverState.message}")
                return GetUserInfoState.Error(serverState.message) // Возвращаем ошибку сервера
            }

            else -> {
                Log.d("ServerRepository: getUserInfo", "Состояние сервера не определено")
                return GetUserInfoState.Idle // Возвращаем Idle, если сервер не был проверен
            }
        }
    }

    suspend fun updateUserInfo(updateUser: UpdateUser): UpdateUserInfoState{
        return when (val serverState = ServerRepository(applicationContext).checkServerStatus()) {
            is ServerState.Success -> {
                if (serverState.isServerOnline) {
                    try {
                        Log.d("ServerRepository: updateUserInfo", "Отправление запроса")

                        val response: Response<ServerResponse> = apiService.updateUserInfo(updateUser)
                        Log.d("ServerRepository: updateUserInfo", "Запрос отправлен")

                        if (response.isSuccessful && response.body() != null) {
                            Log.i("ServerRepository: updateUserInfo", "Данные получены")
                            val responseBody = response.body()!!
                            return UpdateUserInfoState.Success(responseBody.result) // Возвращаем успешный результат
                        } else {
                            return when (response.code()) {
                                400 -> {
                                    Log.w("UserInfoRepository: updateUserInfo", "Некорректный запрос")
                                    UpdateUserInfoState.Error("Некорректный запрос")
                                }

                                409 -> {
                                    Log.w(
                                        "UserInfoRepository: updateUserInfo",
                                        "Неверный токен"
                                    )
                                    UpdateUserInfoState.Error("Неверный токен")
                                }

                                500 -> {
                                    Log.e("UserInfoRepository: updateUserInfo", "Ошибка сервера")
                                    UpdateUserInfoState.Error("Ошибка сервера")
                                }

                                else -> {
                                    Log.e(
                                        "UserInfoRepository: updateUserInfo",
                                        "Неизвестная ошибка: ${response.code()}"
                                    )
                                    UpdateUserInfoState.Error("Неизвестная ошибка: ${response.code()}")
                                }
                            }
                        }
                    } catch (e: IOException) {
                        Log.e("UserInfoRepository: updateUserInfo", "Ошибка сети: ${e.message}")
                        return UpdateUserInfoState.Error("Сервер недоступен") // Возвращаем ошибку сети
                    } catch (e: HttpException) {
                        Log.e("UserInfoRepository: updateUserInfo", "HTTP ошибка: ${e.message}")
                        return UpdateUserInfoState.Error("Сервер недоступен") // Возвращаем HTTP ошибку
                    } catch (e: Exception) {
                        Log.e("UserInfoRepository: updateUserInfo", "Неизвестная ошибка: ${e.message}")
                        return UpdateUserInfoState.Error("Сервер недоступен") // Возвращаем неизвестную ошибку
                    }
                } else {
                    Log.d("UserInfoRepository: updateUserInfo", "Сервер недоступен")
                    return UpdateUserInfoState.Error("Сервер недоступен") // Возвращаем ошибку, если сервер оффлайн
                }
            }

            is ServerState.Error -> {
                Log.d("UserInfoRepository: updateUserInfo", "Ошибка сервера: ${serverState.message}")
                return UpdateUserInfoState.Error(serverState.message) // Возвращаем ошибку сервера
            }

            else -> {
                Log.d("UserInfoRepository: updateUserInfo", "Состояние сервера не определено")
                return UpdateUserInfoState.Idle // Возвращаем Idle, если сервер не был проверен
            }
        }
    }

}
