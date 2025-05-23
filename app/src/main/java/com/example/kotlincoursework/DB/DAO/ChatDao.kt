package com.example.kotlincoursework.DB.DAO

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.example.kotlincoursework.DB.DatabaseHelper
import com.example.kotlincoursework.DB.Models.Chat
import dataBase.User

class ChatDao(private val db: SQLiteDatabase) {
    companion object {
        private const val TABLE_NAME = "chats"
        private const val COL_ID = "chat_id"
        private const val COL_USER_PHONE = "user_phone"
        private const val COL_CONTACT_PHONE = "contact_phone"
        private const val COL_CREATED_AT = "created_at"
        private const val COL_LAST_MSG = "last_message_at"
    }

    fun getOrCreateChat(userPhone: String, contactPhone: String): Long {
        return getChatId(userPhone, contactPhone) ?: createChat(userPhone, contactPhone)
    }

    private fun getChatId(userPhone: String, contactPhone: String): Long? {
        db.query(
            TABLE_NAME,
            arrayOf(COL_ID),
            "$COL_USER_PHONE = ? AND $COL_CONTACT_PHONE = ? OR $COL_USER_PHONE = ? AND $COL_CONTACT_PHONE = ?",
            arrayOf(userPhone, contactPhone, contactPhone, userPhone),
            null, null, null
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.getLong(0) else null
        }
    }

    fun getChatByPhoneNumbers(userPhone: String, contactPhone: String): Chat? {
        db.query(
            TABLE_NAME,
            null, // все столбцы
            "($COL_USER_PHONE = ? AND $COL_CONTACT_PHONE = ?) OR ($COL_USER_PHONE = ? AND $COL_CONTACT_PHONE = ?)",
            arrayOf(userPhone, contactPhone, contactPhone, userPhone),
            null, null, null
        ).use { cursor ->
            return if (cursor.moveToFirst()) {
                Chat(
                    chatId = cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)),
                    userPhone = cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_PHONE)),
                    contactPhone = cursor.getString(cursor.getColumnIndexOrThrow(COL_CONTACT_PHONE)),
                    createdAt = cursor.getLong(cursor.getColumnIndexOrThrow(COL_CREATED_AT)),
                    lastMessageAt = if (cursor.isNull(cursor.getColumnIndexOrThrow(COL_LAST_MSG))) null
                    else cursor.getLong(cursor.getColumnIndexOrThrow(COL_LAST_MSG))
                )
            } else {
                null
            }
        }
    }

    fun createChat(userPhone: String, contactPhone: String): Long {
        val values = ContentValues().apply {
            put(COL_USER_PHONE, userPhone)
            put(COL_CONTACT_PHONE, contactPhone)
            put(COL_CREATED_AT, System.currentTimeMillis())
        }
        return db.insert(TABLE_NAME, null, values)
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

    fun chatExists(userPhone: String, contactPhone: String): Boolean {
        val query = """
        SELECT COUNT(*) 
        FROM $TABLE_NAME 
        WHERE ($COL_USER_PHONE = ? AND $COL_CONTACT_PHONE = ?)
           OR ($COL_USER_PHONE = ? AND $COL_CONTACT_PHONE = ?)
    """.trimIndent()

        db.rawQuery(query, arrayOf(userPhone, contactPhone, contactPhone, userPhone)).use { cursor ->
            return if (cursor.moveToFirst()) {
                cursor.getInt(0) > 0
            } else {
                false
            }
        }
    }


    // Очистка таблицы
    fun clearChatsTable(): Int {
        return db.delete(TABLE_NAME, null, null)
    }
}