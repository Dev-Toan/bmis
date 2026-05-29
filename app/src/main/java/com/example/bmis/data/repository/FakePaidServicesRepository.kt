package com.example.bmis.data.repository

import com.example.bmis.data.models.ItemsServices
import com.example.bmis.data.models.PaidServicesFilterOptions

class FakePaidServicesRepository : PaidServicesRepository {

    override suspend fun getInvoices(statusType: String): List<ItemsServices> = when (statusType) {
        "WARNING" -> listOf(
            ItemsServices(
                id = "0",
                registrationCode = "DK00000",
                servicesName = "Dịch vụ GYM, Dịch vụ bơi lội",
                registrationDate = "21/09/2019",
                paymentDate = "",
                totalAmount = "810.000 VNĐ",
                apartment = "CH002"
            ),
            ItemsServices(
                id = "1",
                registrationCode = "DK00001",
                servicesName = "Dịch vụ GYM",
                registrationDate = "15/09/2025",
                paymentDate = "",
                totalAmount = "750.000 VNĐ",
                apartment = "CH003"
            )
        )
        "ERROR" -> listOf(
            ItemsServices(
                id = "2",
                registrationCode = "DK00002",
                servicesName = "Dịch vụ Bơi lội",
                registrationDate = "10/08/2025",
                paymentDate = "12/08/2025",
                totalAmount = "750.000 VNĐ",
                apartment = "CH004"
            )
        )
        else -> listOf(
            ItemsServices(
                id = "1",
                registrationCode = "DK-20260101-001",
                servicesName = "Dịch vụ GYM",
                registrationDate = "01/09/2025",
                paymentDate = "05/09/2025",
                totalAmount = "750.000 VNĐ",
                apartment = "CH002"
            ),
            ItemsServices(
                id = "2",
                registrationCode = "DK-20260102-002",
                servicesName = "Dịch vụ Bơi lội",
                registrationDate = "02/09/2025",
                paymentDate = "06/09/2025",
                totalAmount = "750.000 VNĐ",
                apartment = "CH003"
            ),
            ItemsServices(
                id = "3",
                registrationCode = "DK-20260103-003",
                servicesName = "Dịch vụ GYM + Bơi lội",
                registrationDate = "03/09/2025",
                paymentDate = "07/09/2025",
                totalAmount = "1.500.000 VNĐ",
                apartment = "CH002"
            )
        )
    }

    override suspend fun getFilterOptions(): PaidServicesFilterOptions = PaidServicesFilterOptions(
        serviceNames = listOf("Dịch vụ GYM", "Dịch vụ Bơi lội"),
        apartmentOptions = listOf("CH002", "CH003", "CH004", "CH005", "CH006"),
        defaultTime = "Tháng 9 - 2025",
        defaultServices = setOf("Dịch vụ GYM"),
        defaultApartment = "CH002"
    )
}
