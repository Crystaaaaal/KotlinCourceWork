package com.example.kotlincoursework.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "messenger.db"
        const val DATABASE_VERSION = 1

        // Таблица users
        object Users : BaseColumns {
            const val TABLE_NAME = "users"
            const val COL_PHONE = "phone_number"
            const val COL_NAME = "full_name"
            const val COL_LOGIN = "login"
            const val COL_AVATAR = "profile_image"
            const val COL_CREATED_AT = "created_at"
        }

        // Таблица chats
        object Chats : BaseColumns {
            const val TABLE_NAME = "chats"
            const val COL_ID = "chat_id"
            const val COL_USER_PHONE = "user_phone"
            const val COL_CONTACT_PHONE = "contact_phone"
            const val COL_CREATED_AT = "created_at"
            const val COL_LAST_MSG = "last_message_at"
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Включаем поддержку внешних ключей
        db.execSQL("PRAGMA foreign_keys = ON")

        // Создаем таблицу пользователей
        db.execSQL("""
            CREATE TABLE ${Users.TABLE_NAME} (
                ${Users.COL_PHONE} TEXT PRIMARY KEY,
                ${Users.COL_NAME} TEXT NOT NULL,
                ${Users.COL_LOGIN} TEXT NOT NULL,
                ${Users.COL_AVATAR} BLOB,
                ${Users.COL_CREATED_AT} TEXT NOT NULL
            )
        """)

        // Создаем таблицу чатов
        db.execSQL("""
            CREATE TABLE ${Chats.TABLE_NAME} (
                ${Chats.COL_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
                ${Chats.COL_USER_PHONE} TEXT NOT NULL,
                ${Chats.COL_CONTACT_PHONE} TEXT NOT NULL,
                ${Chats.COL_CREATED_AT} TEXT NOT NULL,
                ${Chats.COL_LAST_MSG} TEXT,
                FOREIGN KEY (${Chats.COL_USER_PHONE}) 
                    REFERENCES ${Users.TABLE_NAME}(${Users.COL_PHONE}) ON DELETE CASCADE,
                FOREIGN KEY (${Chats.COL_CONTACT_PHONE}) 
                    REFERENCES ${Users.TABLE_NAME}(${Users.COL_PHONE}) ON DELETE CASCADE,
                UNIQUE (${Chats.COL_USER_PHONE}, ${Chats.COL_CONTACT_PHONE})
            )
        """)


        // Создаем индексы для ускорения запросов
        db.execSQL("CREATE INDEX idx_chats_user ON ${Chats.TABLE_NAME}(${Chats.COL_USER_PHONE})")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${Chats.TABLE_NAME}")
        db.execSQL("DROP TABLE IF EXISTS ${Users.TABLE_NAME}")
        onCreate(db)
    }

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        // Включаем внешние ключи для каждой сессии
        db.setForeignKeyConstraintsEnabled(true)
    }
}