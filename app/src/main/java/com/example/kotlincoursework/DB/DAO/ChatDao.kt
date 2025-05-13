package com.example.kotlincoursework.DB.DAO

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.kotlincoursework.DB.DatabaseHelper
import com.example.kotlincoursework.DB.Models.Chat
import dataBase.User

class ChatDao(private val db: SQLiteDatabase) {
    fun getOrCreateChat(userPhone: String, contactPhone: String): Long {
        return getChatId(userPhone, contactPhone) ?: createChat(userPhone, contactPhone)
    }

    private fun getChatId(userPhone: String, contactPhone: String): Long? {
        db.query(
            "chats",
            arrayOf("chat_id"),
            "user_phone = ? AND contact_phone = ?",
            arrayOf(userPhone, contactPhone),
            null, null, null
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.getLong(0) else null
        }
    }

    fun createChat(userPhone: String, contactPhone: String):Long  {
        val values = ContentValues().apply {
            put("user_phone", userPhone)
            put("contact_phone", contactPhone)
            put("created_at", System.currentTimeMillis())
        }
        return db.insert("chats", null, values)
    }
    fun getContactsFromChats(): List<User> {
        val contacts = mutableListOf<User>()

        // Запрос для получения всех уникальных contact_phone из чатов
        val query = """
        SELECT DISTINCT u.* 
        FROM ${DatabaseHelper.Companion.Users.TABLE_NAME} u
        WHERE u.${DatabaseHelper.Companion.Users.COL_PHONE} IN (
        SELECT ${DatabaseHelper.Companion.Chats.COL_USER_PHONE} FROM ${DatabaseHelper.Companion.Chats.TABLE_NAME}
        UNION
        SELECT ${DatabaseHelper.Companion.Chats.COL_CONTACT_PHONE} FROM ${DatabaseHelper.Companion.Chats.TABLE_NAME})
        ORDER BY u.${DatabaseHelper.Companion.Users.COL_NAME}
    """

        db.rawQuery(query, null).use { cursor ->
            while (cursor.moveToNext()) {
                contacts.add(
                    User(
                        phoneNumber = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Companion.Users.COL_PHONE)),
                        hashPassword = "",
                        fullName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Companion.Users.COL_NAME)),
                        login = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Companion.Users.COL_LOGIN)),
                        profileImage = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.Companion.Users.COL_AVATAR)),
                        createdAt = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.Companion.Users.COL_CREATED_AT))
                    )
                )
            }
        }

        return contacts
    }
}