package com.example.bmis.data.repository

import com.example.bmis.data.models.ItemsServices
import com.example.bmis.data.models.PaidServicesFilterOptions

interface PaidServicesRepository {
    suspend fun getInvoices(statusType: String): List<ItemsServices>
    suspend fun getFilterOptions(): PaidServicesFilterOptions
}
