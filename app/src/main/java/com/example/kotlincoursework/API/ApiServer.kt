package com.example.kotlincoursework.API



import dataBase.ActiveUser
import dataBase.LoginRecive
import dataBase.TokenAndNumberRecive
import dataBase.LoginUser
import dataBase.PhoneOrLoginRemote
import dataBase.RegistrationUserInfo
import dataBase.SearchingResponse
import dataBase.ServerResponse
import dataBase.UpdateUser
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiServer {
    @GET("serverIsOnline")
    suspend fun checkServerStatus(): Response<ServerResponse>

    @POST("/registration")
    suspend fun registrationUser(@Body user: RegistrationUserInfo): Response<ServerResponse>

    @POST("/login")
    suspend fun loginUser(@Body user: LoginUser): Response<LoginRecive>

    @POST("/search")
    suspend fun searchUser(@Body phoneOnLogin: PhoneOrLoginRemote): Response<SearchingResponse>

    @POST("/sendUserInfo")
    suspend fun getUserInfo(@Body user: TokenAndNumberRecive): Response<ActiveUser>

    @PATCH("/updateUserInfo")
    suspend fun updateUserInfo(@Body updateUser: UpdateUser):Response<ServerResponse>

}