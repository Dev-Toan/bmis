package com.example.bmis.data.repository

import com.example.bmis.data.models.CartItem
import com.example.bmis.data.models.CartRegistrationInfo

interface CartRepository {
    suspend fun getCartItems(): List<CartItem>
    suspend fun getRegistrationInfo(): CartRegistrationInfo
}
