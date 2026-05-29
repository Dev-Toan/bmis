package com.example.bmis.data.repository

import com.example.bmis.data.models.EmployeeInfo
import com.example.bmis.data.models.RegistrationDetail
import com.example.bmis.data.models.RegistrationOrderStatus
import com.example.bmis.data.models.RegistrationTicketItem

class FakeRegistrationRepository : RegistrationRepository {

    private val defaultTickets = listOf(
        RegistrationTicketItem(
            id = "1",
            title = "Dịch vụ GYM",
            packageName = "Gói cơ bản 1 tháng",
            price = "750.000 VNĐ",
            quantity = 1,
            iconType = "fitness"
        ),
        RegistrationTicketItem(
            id = "2",
            title = "Dịch vụ Bơi lội",
            packageName = "Gói cơ bản 1 tháng",
            price = "750.000 VNĐ",
            quantity = 1,
            iconType = "pool"
        )
    )

    private val employee = EmployeeInfo(
        name = "Nguyễn Văn A",
        phone = "0924535658",
        email = "ngqtung@gmail.com"
    )

    override suspend fun getRegistrationDetail(
        registrationId: String,
        status: RegistrationOrderStatus
    ): RegistrationDetail? {
        val code = registrationId.ifBlank { "DK0001" }.let {
            if (it.startsWith("DK")) it else "DK$it"
        }

        return when (status) {
            RegistrationOrderStatus.PENDING -> RegistrationDetail(
                registrationId = registrationId,
                status = status,
                tickets = defaultTickets,
                registrationCode = code,
                totalAmount = "1.500.000 VNĐ",
                apartment = "CH003",
                phone = "0912345678",
                registrationDate = "27/09/2025",
                paymentDate = "",
                paymentMethod = "",
                cancellationDate = "",
                note = "Không có ghi chú",
                employee = null
            )
            RegistrationOrderStatus.PAID -> RegistrationDetail(
                registrationId = registrationId,
                status = status,
                tickets = defaultTickets,
                registrationCode = code,
                totalAmount = "1.500.000 VNĐ",
                apartment = "CH003",
                phone = "0912345678",
                registrationDate = "27/09/2025",
                paymentDate = "27/09/2025",
                paymentMethod = "VNPT PAY",
                cancellationDate = "",
                note = "Không có ghi chú",
                employee = employee
            )
            RegistrationOrderStatus.CANCELLED -> RegistrationDetail(
                registrationId = registrationId,
                status = status,
                tickets = defaultTickets,
                registrationCode = code,
                totalAmount = "1.500.000 VNĐ",
                apartment = "CH003",
                phone = "0912345678",
                registrationDate = "27/09/2025",
                paymentDate = "27/09/2025",
                paymentMethod = "",
                cancellationDate = "27/09/2025",
                note = "Không có ghi chú",
                employee = employee
            )
        }
    }
}
