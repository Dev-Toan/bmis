package com.example.bmis.data.models

data class CartItem(
    val id: String,
    val title: String,
    val packageName: String,
    val price: String,
    val iconType: String
)

data class CartRegistrationInfo(
    val registrantName: String,
    val apartmentOptions: List<String>,
    val defaultApartment: String,
    val defaultPhone: String,
    val paymentMethods: List<String> = listOf(
        "VNPT PAY",
        "Thanh toán tại quầy",
        "Thanh toán tại nhà"
    )
)
