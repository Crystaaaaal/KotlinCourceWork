package com.example.kotlincoursework.API


import dataBase.SearchingResponse
import dataBase.LoginUser
import dataBase.PhoneOrLoginRemote
import dataBase.RegistrationUserInfo
import dataBase.ServerResponse
import dataBase.loginRecive
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServer {
    @GET("serverIsOnline")
    suspend fun checkServerStatus(): Response<ServerResponse>

    @POST("/registration")
    suspend fun registrationUser(@Body user: RegistrationUserInfo): Response<ServerResponse>

    @POST("/login")
    suspend fun loginUser(@Body user: LoginUser): Response<loginRecive>

    @POST("/search")
    suspend fun searchUser(@Body phoneOnLogin: PhoneOrLoginRemote): Response<SearchingResponse>

}