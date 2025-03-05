package com.example.kotlincoursework.API

import android.util.Log
import dataBase.ServerResponse
import retrofit2.Call
import retrofit2.HttpException
import java.io.IOException

class ServerRepository {
    private val apiService = ApiClient.apiService

    suspend fun checkServerStatus(): Boolean {
        return try {
            val response = apiService.checkServerStatus()
            if (response.isSuccessful) {
                true
            } else {
                false
            }
        } catch (e: IOException) {
            Log.d("checkServerStatus", "Ошибка сети!")
            false
        } catch (e: HttpException) {
            Log.d("checkServerStatus", "HTTP ошибка!")
            false
        } catch (e: Exception) {
            Log.d("checkServerStatus", "Неизвестная ошибка!")
            false
        }
    }
}
