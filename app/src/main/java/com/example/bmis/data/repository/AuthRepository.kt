package com.example.bmis.data.repository

import com.example.bmis.data.api.ApiService
import com.example.bmis.data.models.UserResponse

class AuthRepository(private val apiService: ApiService) {

    suspend fun getUserByPhone(phoneNumber: String): UserResponse? {
        return try {
            val users = apiService.getUsers()
            users.find { it.phone == phoneNumber }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


   fun validatePassword(userId: Int, password: String): Boolean {
        return password == "1"
    }


    fun saveUserId(userId: Int, context: android.content.Context) {
        val sharedPref = context.getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putInt("userId", userId)
            apply()
        }
    }


    fun getUserId(context: android.content.Context): Int {
        val sharedPref = context.getSharedPreferences("UserPrefs", android.content.Context.MODE_PRIVATE)
        return sharedPref.getInt("userId", -1)
    }
}