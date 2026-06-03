package com.example.bmis.data.repository

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.WaterDrop
import com.example.bmis.data.models.RepairCategory
import com.example.bmis.data.models.RepairRequest
import com.example.bmis.data.models.RepairStatus

class FakeRepairRepository : RepairRepository {
    override suspend fun getRepairCategories() = listOf(
        RepairCategory(1, "Sửa chữa Điện lạnh", "Cung cấp dịch vụ sửa chữa các...", icon = Icons.Default.AcUnit),
        RepairCategory(2, "Sửa chữa Thiết bị chiếu sáng", "Cung cấp dịch vụ sửa chữa các...", icon = Icons.Default.Lightbulb),
        RepairCategory(3, "Sửa chữa Điện nước", "Cung cấp dịch vụ sửa chữa các...", icon = Icons.Default.WaterDrop),
    )

    override suspend fun getRepairRequests(status: RepairStatus): List<RepairRequest> {
        return listOf(
            RepairRequest(
                "1",
                "DK00000",
                "Sửa chữa điện lạnh",
                "1307",
                "21/08/2019",
                status = status
            )
        )
    }
}