package com.example.kotlincoursework.API

import android.util.Log
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.kotlincoursework.viewModel.viewModel
import com.google.gson.Gson
import dataBase.LoginRecive
import dataBase.Message
import dataBase.MessageIncoming
import dataBase.User
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
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
    private val gson = Gson()
    private var webSocket: WebSocket? = null
    //private var BASE_URL = "http://192.168.11.107:8080/"
    private const val BASE_URL = "http://10.0.2.2:8080/"
    private const val WS_URL = "ws://10.0.2.2:8080/ws/"
    private const val TIMEOUT = 30L

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

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



    fun startWebSocket(phoneNumber: String,viewModel: viewModel) {
        val request = Request.Builder()
            .url("$WS_URL$phoneNumber")
            .build()

        val listener = object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: Response) {
                this@ApiClient.webSocket = webSocket
                Log.i("WebSocket:onOpen","WebSocket подключен для $phoneNumber")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.i("WebSocket:onMessage","Получено сообщение")
                val message = gson.fromJson(text, MessageIncoming::class.java)
                Log.i("WebSocket:onMessage","для: ${message.forUser} " +
                        "от: ${message.fromUser}, " +
                        "сообщение: ${message.messageText} " +
                        "время: ${message.sentAt}")

                // Обработка входящих сообщений
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                println("Соединение закрывается: $reason")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                println("Ошибка соединения: ${t.message}")
            }
        }

        OkHttpClient().newWebSocket(request, listener)
    }

    // Функция для отправки сообщений
    fun sendMessage(forUser:User,fromUser: LoginRecive,text: String, sentAt: String) {
        val message = Message(
            forUser = forUser,
            fromUser = fromUser,
            messageText = text,
            sentAt = sentAt
            //LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
        val jsonMessage = gson.toJson(message)
        webSocket?.send(jsonMessage)  // Отправляем, если WebSocket активен
    }

    fun disconnect() {
        webSocket?.close(1000, "Пользователь вышел")
    }

}

// Закрытие ExecutorService не требуется, так как OkHttpClient управляет
