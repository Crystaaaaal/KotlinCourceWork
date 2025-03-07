package com.example.kotlincoursework.API


import dataBase.RegistrationUserInfo
import dataBase.ServerResponse
import dataBase.Token
import dataBase.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServer {
    @GET("serverIsOnline")
    suspend fun checkServerStatus(): Response<ServerResponse>

    @POST("/registration")
    suspend fun registrationUser(@Body user: RegistrationUserInfo): Response<ServerResponse>

}