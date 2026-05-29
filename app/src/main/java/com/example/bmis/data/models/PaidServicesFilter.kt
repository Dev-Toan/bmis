package com.example.bmis.data.models

enum class SortOption(val title: String) {
    ALL("Tất cả"),
    AMOUNT_ASC("Tổng tiền tăng dần"),
    AMOUNT_DESC("Tổng tiền giảm dần"),
    DATE_NEWEST("Ngày đăng ký mới nhất"),
    DATE_OLDEST("Ngày đăng ký cũ nhất")
}

enum class FilterStep {
    MAIN,
    TIME,
    SERVICES,
    APARTMENT
}

data class PaidServicesFilterOptions(
    val serviceNames: List<String>,
    val apartmentOptions: List<String>,
    val defaultTime: String?,
    val defaultServices: Set<String>,
    val defaultApartment: String?
)
