package com.example.bmis.data.repository

import com.example.bmis.data.models.ItemsServices
import com.example.bmis.data.models.PaidServicesFilterOptions

class FakePaidServicesRepository : PaidServicesRepository {

    override suspend fun getInvoices(statusType: String): List<ItemsServices> = when (statusType) {
        "WARNING" -> listOf( // Chưa thanh toán
            ItemsServices(
                id = "101",
                registrationCode = "DK00000",
                servicesName = "Sửa chữa điện lạnh",
                registrationDate = "21/09/2019",
                paymentDate = "",
                totalAmount = "500.000 VNĐ",
                apartment = "1307"
            ),
            ItemsServices(
                id = "102",
                registrationCode = "DK00000",
                servicesName = "Sửa chữa điện lạnh",
                registrationDate = "21/09/2019",
                paymentDate = "",
                totalAmount = "500.000 VNĐ",
                apartment = "1307"
            ),
            ItemsServices(
                id = "103",
                registrationCode = "DK00000",
                servicesName = "Sửa chữa điện lạnh",
                registrationDate = "21/09/2019",
                paymentDate = "",
                totalAmount = "500.000 VNĐ",
                apartment = "1307"
            )
        )
        "ERROR" -> listOf( // Đã hủy
            ItemsServices(
                id = "201",
                registrationCode = "DK00000",
                servicesName = "Sửa chữa điện lạnh",
                registrationDate = "21/09/2019",
                paymentDate = "",
                totalAmount = "500.000 VNĐ",
                apartment = "1307"
            ),
            ItemsServices(
                id = "202",
                registrationCode = "DK00000",
                servicesName = "Sửa chữa điện lạnh",
                registrationDate = "21/09/2019",
                paymentDate = "",
                totalAmount = "500.000 VNĐ",
                apartment = "1307"
            )
        )
        else -> listOf( // Đã thanh toán (SUCCESS)
            ItemsServices(
                id = "301",
                registrationCode = "DK00000",
                servicesName = "Sửa chữa điện lạnh",
                registrationDate = "21/09/2019",
                paymentDate = "21/09/2019",
                totalAmount = "500.000 VNĐ",
                apartment = "1307"
            )
        )
    }

    override suspend fun getFilterOptions(): PaidServicesFilterOptions = PaidServicesFilterOptions(
        serviceNames = listOf("Sửa chữa điện lạnh", "Dịch vụ GYM", "Dịch vụ Bơi lội"),
        apartmentOptions = listOf("1307", "CH002", "CH003", "CH004"),
        defaultTime = null,
        defaultServices = emptySet(),
        defaultApartment = null
    )
}
