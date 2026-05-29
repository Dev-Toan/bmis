package com.example.bmis.data.models

data class ItemsServices(
    val id: String,
    val registrationCode: String,
    val servicesName: String,
    val registrationDate: String,
    val paymentDate: String,
    val totalAmount: String,
    val apartment: String = "CH002"
)