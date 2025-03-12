package com.example.kotlincoursework.API

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = " http://10.0.2.2:8080/" //10.0.2.2  192.168.10.150

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL) // Базовый URL сервера
        .addConverterFactory(GsonConverterFactory.create()) // Конвертер для JSON
        .build()

    val apiService = retrofit.create(ApiServer::class.java)
}