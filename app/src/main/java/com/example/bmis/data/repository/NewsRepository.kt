package com.example.bmis.data.repository

import com.example.bmis.data.api.ApiService
import com.example.bmis.data.models.NewsArticleDetail
import com.example.bmis.data.models.Post

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

    suspend fun getNewsArticle(postId: Int): NewsArticleDetail? {
        val post = getPostById(postId) ?: return null
        return NewsArticleDetail(
            id = post.id,
            title = post.title,
            content = post.body,
            publishedAt = "10/09/2025  03:05:06",
            imageResName = "chuachay"
        )
    }
}