package com.example.bmis.data.api

import com.example.bmis.data.models.Post
import com.example.bmis.data.models.Todo
import com.example.bmis.data.models.UserResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<UserResponse>

    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("todos")
    suspend fun getTodosByUserId(@Query("userId") userId: Int): List<Todo>

}