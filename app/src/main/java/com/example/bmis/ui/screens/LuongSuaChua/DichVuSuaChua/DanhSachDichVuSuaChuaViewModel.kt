package com.example.bmis.ui.screens.LuongSuaChua.DichVuSuaChua

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.models.RepairCategory
import com.example.bmis.data.models.RepairRequest
import com.example.bmis.data.models.RepairStatus
import com.example.bmis.data.repository.FakeRepairRepository
import com.example.bmis.data.repository.RepairRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ServicesUiState(
    val isLoading: Boolean = false,
    val services: List<RepairCategory> = emptyList(),
    val statuses: List<RepairRequest> = emptyList(),
    val error: String? = null
)

class DanhSachDichVuSuaChuaViewModel(
    private val repairRepository: RepairRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServicesUiState(isLoading = true))
    val uiState: StateFlow<ServicesUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val services = repairRepository.getRepairCategories()
                val statuses = repairRepository.getRepairRequests(RepairStatus.PENDING)
                _uiState.value = _uiState.value.copy(isLoading = false, services = services, statuses = statuses)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = t.message ?: "Lỗi")
            }
        }
    }

    fun onServiceClicked(service: RepairCategory) {
        // Handle service click
    }

    fun onStatusClicked(status: RepairRequest) {
        // Handle status click
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = FakeRepairRepository()
                return DanhSachDichVuSuaChuaViewModel(repository) as T
            }
        }
    }
}