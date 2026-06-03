package com.example.bmis.data.models

import androidx.compose.ui.graphics.vector.ImageVector

enum class RepairStatus {
    PENDING, RECEIVED, PROCESSED
}

data class RepairRequest(
    val id: String,
    val registrationCode: String,
    val serviceName: String,
    val apartment: String,
    val registrationDate: String,
    val repairEmployee: String? = null,
    val repairResult: String? = null,
    val status: RepairStatus,
    val icon: ImageVector? = null
)

data class RepairCategory(
    val id: Int,
    val name: String,
    val description: String,
    val icon: ImageVector? = null
)
