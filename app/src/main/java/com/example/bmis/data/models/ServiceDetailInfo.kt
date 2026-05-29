package com.example.bmis.data.models

data class ServiceDetailInfo(
    val serviceId: String,
    val title: String,              // "Dịch vụ GYM"
    val description: String,        // "Cung cấp dịch vụ GYM"
    val registeredCount: Int,     // 250
    val bannerImageResName: String, // "gym" — Screen map sang R.drawable
    val iconType: String,           // "fitness" | "pool" — Screen map sang Icon
    val galleryIndex: String        // "1/5"
)