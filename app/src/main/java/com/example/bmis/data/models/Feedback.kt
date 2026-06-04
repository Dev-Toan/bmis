package com.example.bmis.data.models

import androidx.compose.ui.graphics.Color

enum class FeedbackStatus(val title: String, val color: Color) {
    WAITING("Chờ phản hồi", Color(0xFFF44336)),
    PROCESSING("Đã phản hồi", Color(0xFFFF9800)),
    RESOLVED("Đã phản hồi", Color(0xFF4CAF50))
}

data class FeedbackItem(
    val id: String,
    val title: String,
    val apartment: String,
    val type: String,
    val content: String,
    val status: FeedbackStatus,
    val date: String
)

data class Comment(
    val id: String,
    val userName: String,
    val content: String,
    val timestamp: String,
    val isStaff: Boolean = false,
    val attachedImages: List<String> = emptyList()
)

data class FeedbackDetail(
    val id: String,
    val title: String,
    val status: FeedbackStatus,
    val apartment: String,
    val type: String,
    val content: String,
    val attachedImages: List<String>,
    val comments: List<Comment>
)
