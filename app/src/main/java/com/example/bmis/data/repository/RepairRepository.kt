package com.example.bmis.data.repository

import com.example.bmis.data.models.RepairCategory
import com.example.bmis.data.models.RepairRequest
import com.example.bmis.data.models.RepairStatus

interface RepairRepository {
    suspend fun getRepairCategories(): List<RepairCategory>
    suspend fun getRepairRequests(status: RepairStatus): List<RepairRequest>
}
