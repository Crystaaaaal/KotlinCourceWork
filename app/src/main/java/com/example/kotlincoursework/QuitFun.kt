package com.example.kotlincoursework

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.kotlincoursework.API.ApiClient
import com.example.kotlincoursework.API.Repositorys.QuitRepository
import com.example.kotlincoursework.DB.DAO.ChatDao
import com.example.kotlincoursework.DB.DAO.UserDao
import dataBase.TokenAndNumberRecive
import kotlinx.coroutines.CoroutineScope
import org.koin.mp.KoinPlatform.getKoin

object QuitObj {
    suspend fun quit(context: Context){
        val masterKey =
            MasterKey.Builder(context, MasterKey.DEFAULT_MASTER_KEY_ALIAS)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()

        val sharedPreferences = EncryptedSharedPreferences.create(
            context,
            "secure_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
        val token = sharedPreferences.getString("auth_token", null)
        val phoneNumber = sharedPreferences.getString("auth_phone", null)
        if (!token.isNullOrEmpty() && !phoneNumber.isNullOrEmpty()) {
            QuitRepository().quit(TokenAndNumberRecive(token!!, phoneNumber!!))
        }
        sharedPreferences.edit().clear().apply()
        ApiClient.disconnect()
        val historySharedPreferences = context.getSharedPreferences("USER_HISTORY", Context.MODE_PRIVATE)
        historySharedPreferences.edit().clear().apply()

        val chatDao = getKoin().get<ChatDao>()
        chatDao.clearChatsTable()

        val userDao = getKoin().get<UserDao>()
        userDao.clearUsersTable()
    }


}