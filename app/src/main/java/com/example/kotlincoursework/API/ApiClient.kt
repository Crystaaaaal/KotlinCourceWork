package com.example.kotlincoursework.API

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//object ApiClient {
//    private const val BASE_URL = " http://10.0.2.2:8080/" //10.0.2.2  192.168.10.150
//
//    val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL) // Базовый URL сервера
//        .addConverterFactory(GsonConverterFactory.create()) // Конвертер для JSON
//        .build()
//
//    val apiService = retrofit.create(ApiServer::class.java)
//}

object ApiClient {
    private const val BASE_URL = "http://192.168.10.150:8080/"
    private const val TIMEOUT = 30L

//    private val okHttpClient = OkHttpClient.Builder()
//        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
//        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
//        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
//        .addInterceptor(HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        })
//        .build()

    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            //.client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiServer by lazy {
        retrofit.create(ApiServer::class.java)
    }
}
