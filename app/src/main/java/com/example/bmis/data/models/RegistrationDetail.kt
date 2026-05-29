package com.example.bmis.data.models

enum class RegistrationOrderStatus {
    PENDING,
    PAID,
    CANCELLED;

    val title: String
        get() = when (this) {
            PENDING -> "Chưa thanh toán"
            PAID -> "Đã thanh toán"
            CANCELLED -> "Đã hủy"
        }
}

data class RegistrationTicketItem(
    val id: String,
    val title: String,
    val packageName: String,
    val price: String,
    val quantity: Int,
    val iconType: String
)

data class EmployeeInfo(
    val name: String,
    val phone: String,
    val email: String
)

data class RegistrationDetail(
    val registrationId: String,
    val status: RegistrationOrderStatus,
    val tickets: List<RegistrationTicketItem>,
    val registrationCode: String,
    val totalAmount: String,
    val apartment: String,
    val phone: String,
    val registrationDate: String,
    val paymentDate: String,
    val paymentMethod: String,
    val cancellationDate: String,
    val note: String,
    val employee: EmployeeInfo?
)
