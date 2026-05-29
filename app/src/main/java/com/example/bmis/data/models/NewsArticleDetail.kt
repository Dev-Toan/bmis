package com.example.bmis.data.models

data class NewsArticleDetail(
    val id: Int,
    val title: String,
    val content: String,
    val publishedAt: String,
    val imageResName: String = "chuachay"
)
