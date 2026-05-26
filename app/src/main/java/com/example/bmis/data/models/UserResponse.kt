package com.example.bmis.data.models


data class UserResponse(
    val id: Int,
    val name: String,
    val phone: String,
    val username: String? = null,
    val email: String? = null
)