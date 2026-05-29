package com.example.bmis.data.models

data class ServicePackage(
    val id: String,
    val title: String,
    val registrationCount: Int,
    val currentPrice: String,
    val originalPrice: String,
    val discountPercent: String
)