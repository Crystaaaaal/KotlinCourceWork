package com.example.kotlincoursework.DB.DAO

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import dataBase.User

class UserDao(private val db: SQLiteDatabase) {

    fun getOrCreateUser(user: User): User {
        if (getUser(user.phoneNumber) == null){
            return createUser(user)
        }
        else {
            return getUser(user.phoneNumber)!!
        }
    }

    fun getUser(phone: String): User? {
        db.query(
            "users",
            arrayOf("phone_number", "full_name", "login", "profile_image", "created_at"),
            "phone_number = ?",
            arrayOf(phone),
            null, null, null
        ).use { cursor ->
            return if (cursor.moveToFirst()) {
                User(
                    phoneNumber = cursor.getString(0),
                    hashPassword = "",
                    fullName = cursor.getString(1),
                    login = cursor.getString(2),
                    profileImage = cursor.getBlob(3),
                    createdAt = cursor.getString(4)
                )
            } else null
        }
    }
    fun createUser(user: User):User {
        val values = ContentValues().apply {
            put("phone_number", user.phoneNumber)
            put("full_name", user.fullName)
            put("login", user.login)
            put("profile_image", user.profileImage)
            put("created_at", System.currentTimeMillis())
        }
        db.insert("users", null, values)
        return user
    }
}