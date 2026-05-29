package com.example.bmis.data.repository

import com.example.bmis.data.models.CartItem
import com.example.bmis.data.models.CartRegistrationInfo

class FakeCartRepository : CartRepository {

    override suspend fun getCartItems(): List<CartItem> = listOf(
        CartItem(
            id = "1",
            title = "Dịch vụ GYM",
            packageName = "Gói cơ bản 1 tháng",
            price = "750.000 VNĐ",
            iconType = "fitness"
        ),
        CartItem(
            id = "2",
            title = "Dịch vụ Bơi lội",
            packageName = "Gói cơ bản 1 tháng",
            price = "750.000 VNĐ",
            iconType = "pool"
        )
    )

    override suspend fun getRegistrationInfo(): CartRegistrationInfo = CartRegistrationInfo(
        registrantName = "Nguyễn Trung Thành",
        apartmentOptions = listOf("CH002", "CH003", "CH004", "CH005"),
        defaultApartment = "CH002",
        defaultPhone = "0912684123"
    )
}
