package com.example.bmis.data.repository

import com.example.bmis.data.api.ApiService
import com.example.bmis.data.models.Post
import kotlin.collections.find

class NewsRepository(private val apiService: ApiService) {

    suspend fun getPosts(): List<Post> {
        return try {
            apiService.getPosts()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getPostById(postId: Int): Post? {
        return try {
            val posts = apiService.getPosts()
            posts.find { it.id == postId }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}