package com.example.bmis.data.repository


import com.example.bmis.data.api.ApiService
import com.example.bmis.data.models.Todo

class TodoRepository(private val apiService: ApiService) {

    suspend fun getTodosByUserId(userId: Int): List<Todo> {
        return try {
            apiService.getTodosByUserId(userId)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }
}