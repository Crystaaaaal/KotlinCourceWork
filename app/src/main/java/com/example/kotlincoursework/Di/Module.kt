package com.example.kotlincoursework.Di

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.kotlincoursework.DB.DAO.ChatDao
import com.example.kotlincoursework.DB.DAO.UserDao
import com.example.kotlincoursework.DB.DatabaseHelper
import com.example.kotlincoursework.viewModel.viewModel
import dataBase.User
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseModule = module {
    // Singleton базы данных
    single<DatabaseHelper> {
        DatabaseHelper(androidContext())
    }

    // DAO как singleton
    single<ChatDao> {
        ChatDao(get<DatabaseHelper>().writableDatabase)
    }

    single<UserDao> {
        UserDao(get<DatabaseHelper>().writableDatabase)
    }
}

val viewModelModule = module {
    viewModel {
        val context = get<Context>()
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
        val dbHelper = DatabaseHelper(context)

        viewModel(
            applicationContext = context,
            sharedPreferences = sharedPreferences,
            db = dbHelper.writableDatabase,
            dbHelper = dbHelper
        )
    }
}