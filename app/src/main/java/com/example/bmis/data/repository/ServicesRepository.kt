package com.example.bmis.data.repository

import com.example.bmis.data.models.PackageDetail
import com.example.bmis.data.models.ServiceDetailInfo
import com.example.bmis.data.models.ServicePackage
import com.example.bmis.ui.screens.services.RegistrationStatus
import com.example.bmis.ui.screens.services.UtilityService

interface ServicesRepository {
    suspend fun getServices(): List<UtilityService>
    suspend fun getStatuses(): List<RegistrationStatus>
    
    suspend fun getServiceDetail(serviceId: String): ServiceDetailInfo?
    suspend fun getPackages(serviceId: String): List<ServicePackage>
    suspend fun getPackageDetail(serviceId: String, packageId: String): PackageDetail?
}