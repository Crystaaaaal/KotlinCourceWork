package com.example.kotlincoursework.API


import dataBase.ServerResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiServer {
    @GET("serverIsOnline")
    suspend fun checkServerStatus(): Response<ServerResponse>

}