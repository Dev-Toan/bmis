package com.example.bmis.ui.screens.LuongSuaChua.ThongTinChiTietDangKy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.models.RepairStatus
import com.example.bmis.data.repository.RepairRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ThongTinChiTietUiState(
    val isLoading: Boolean = false,
    val registrationId: String = "",
    val status: RepairStatus = RepairStatus.PENDING,
    val serviceName: String = "Sửa chữa điện lạnh",
    val apartment: String = "1307",
    val description: String = "Điều hòa không mát",
    val registrationDate: String = "29/09/2025",
    val receptionStaff: String = "Nguyễn Văn C",
    val receptionDate: String = "29/09/2025",
    val repairStaff: String = "Nguyễn Văn A\nTrần Văn B",
    val processedDate: String = "29/09/2025",
    val repairResult: String = "Đã sửa",
    val errorMessage: String? = null
)

class ThongTinChiTietViewModel(
    private val repository: RepairRepository,
    private val initialRegistrationId: String,
    private val initialStatus: RepairStatus
) : ViewModel() {

    private val _uiState = MutableStateFlow(ThongTinChiTietUiState(
        registrationId = initialRegistrationId,
        status = initialStatus
    ))
    val uiState: StateFlow<ThongTinChiTietUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Giả lập load từ repository
            // val detail = repository.getRepairDetail(registrationId)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    companion object {
        fun provideFactory(
            repository: RepairRepository,
            registrationId: String,
            status: RepairStatus
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ThongTinChiTietViewModel(repository, registrationId, status) as T
            }
        }
    }
}
