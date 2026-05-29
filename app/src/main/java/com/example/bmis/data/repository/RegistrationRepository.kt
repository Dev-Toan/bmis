package com.example.bmis.data.repository

import com.example.bmis.data.models.RegistrationDetail
import com.example.bmis.data.models.RegistrationOrderStatus

interface RegistrationRepository {
    suspend fun getRegistrationDetail(
        registrationId: String,
        status: RegistrationOrderStatus
    ): RegistrationDetail?
}
