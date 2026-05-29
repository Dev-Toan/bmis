package com.example.bmis.data.models

data class PackageDetail(
    val packageId: String,
    val serviceId: String,
    val serviceTitle: String,
    val serviceDescription: String,
    val iconType: String,
    val packageName: String,
    val currentPrice: String,
    val originalPrice: String,
    val discountPercent: String,
    val duration: String,
    val registrationCountLabel: String
)
