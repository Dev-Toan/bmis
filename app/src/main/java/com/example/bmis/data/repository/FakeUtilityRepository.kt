package com.example.bmis.data.repository

import com.example.bmis.ui.screens.services.UtilityService
import com.example.bmis.ui.screens.services.RegistrationStatus
import com.example.bmis.ui.screens.services.StatusType
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pool
import com.example.bmis.ui.theme.PrimaryBlue
import com.example.bmis.data.models.PackageDetail
import com.example.bmis.data.models.ServiceDetailInfo
import com.example.bmis.data.models.ServicePackage

class FakeUtilityRepository : UtilityRepository {
    override suspend fun getServices(): List<UtilityService> = listOf(
        UtilityService(1, "Dịch vụ GYM", "Cung cấp dịch vụ GYM", 250, Icons.Default.FitnessCenter, PrimaryBlue),
        UtilityService(2, "Dịch vụ Bơi lội", "Cung cấp dịch vụ Bơi lội", 250, Icons.Default.Pool, PrimaryBlue)
    )

    override suspend fun getStatuses(): List<RegistrationStatus> = listOf(
        RegistrationStatus(1, "Đã thanh toán", "2 dịch vụ tiện ích", StatusType.SUCCESS),
        RegistrationStatus(2, "Chưa thanh toán", "3 dịch vụ tiện ích", StatusType.WARNING),
        RegistrationStatus(3, "Đã hủy", "2 dịch vụ tiện ích", StatusType.ERROR)
    )

    override suspend fun getServiceDetail(serviceId: String): ServiceDetailInfo? {
        return when (serviceId) {
            "1" -> ServiceDetailInfo(
                serviceId = "1",
                title = "Dịch vụ GYM",
                description = "Cung cấp dịch vụ GYM",
                registeredCount = 250,
                bannerImageResName = "gym",
                iconType = "fitness",
                galleryIndex = "1/5"
            )
            "2" -> ServiceDetailInfo(
                serviceId = "2",
                title = "Dịch vụ Bơi lội",
                description = "Cung cấp dịch vụ Bơi lội",
                registeredCount = 250,
                bannerImageResName = "gym", // tạm; sau thêm drawable pool
                iconType = "pool",
                galleryIndex = "1/5"
            )
            else -> null
        }
    }

    override suspend fun getPackages(serviceId: String): List<ServicePackage> {
        return when (serviceId) {
            "1", "2" -> listOf(
                ServicePackage(
                    id = "1",
                    title = "Gói cơ bản 1 tháng",
                    registrationCount = 100,
                    currentPrice = "750.000 VNĐ",
                    originalPrice = "1.000.000 VNĐ",
                    discountPercent = "-25%"
                ),
                ServicePackage(
                    id = "2",
                    title = "Gói cơ bản 1 tháng",
                    registrationCount = 100,
                    currentPrice = "750.000 VNĐ",
                    originalPrice = "1.000.000 VNĐ",
                    discountPercent = "-25%"
                )
            )
            else -> emptyList()
        }
    }

    override suspend fun getPackageDetail(serviceId: String, packageId: String): PackageDetail? {
        val serviceInfo = getServiceDetail(serviceId) ?: return null
        val packageItem = getPackages(serviceId).find { it.id == packageId } ?: return null

        val description = when (serviceId) {
            "1" -> "Cung cấp cho những thành viên muốn trải nghiệm dịch vụ"
            "2" -> "Cung cấp dịch vụ bơi lội cho cư dân"
            else -> serviceInfo.description
        }

        return PackageDetail(
            packageId = packageItem.id,
            serviceId = serviceId,
            serviceTitle = serviceInfo.title,
            serviceDescription = description,
            iconType = serviceInfo.iconType,
            packageName = packageItem.title,
            currentPrice = packageItem.currentPrice,
            originalPrice = packageItem.originalPrice,
            discountPercent = packageItem.discountPercent,
            duration = "1 tháng",
            registrationCountLabel = "${packageItem.registrationCount} lượt"
        )
    }
}