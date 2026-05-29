package com.example.bmis.ui.screens.services

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.repository.FakeServicesRepository
import com.example.bmis.data.repository.ServicesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ServicesUiState(
    val isLoading: Boolean = false,
    val services: List<UtilityService> = emptyList(),
    val statuses: List<RegistrationStatus> = emptyList(),
    val error: String? = null
)

class ServicesViewModel(
    private val repository: ServicesRepository
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
                val services = repository.getServices()
                val statuses = repository.getStatuses()
                _uiState.value = ServicesUiState(isLoading = false, services = services, statuses = statuses)
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = t.message ?: "Lỗi")
            }
        }
    }

    fun onServiceClicked(service: UtilityService) {
        // Intentionally empty for now: navigation is handled at the composable/navigation layer.
    }

    fun onStatusClicked(status: RegistrationStatus) {
        // Intentionally empty for now: navigation is handled at the composable/navigation layer.
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ServicesViewModel(FakeServicesRepository()) as T
            }
        }
    }
}