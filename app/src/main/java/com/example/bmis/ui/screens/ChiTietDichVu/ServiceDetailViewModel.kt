package com.example.bmis.ui.screens.ChiTietDichVu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.models.ServiceDetailInfo
import com.example.bmis.data.models.ServicePackage
import com.example.bmis.data.repository.FakeServicesRepository
import com.example.bmis.data.repository.ServicesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ServiceDetailUiState(
    val isLoading: Boolean = false,
    val serviceInfo: ServiceDetailInfo? = null,
    val packages: List<ServicePackage> = emptyList(),
    val selectedBottomTab: Int = 1,
    val showAddToCartSuccess: Boolean = false,
    val errorMessage: String? = null
)

class ServiceDetailViewModel(
    private val repository: ServicesRepository,
    private val serviceId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(ServiceDetailUiState(isLoading = true))
    val uiState: StateFlow<ServiceDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val info = repository.getServiceDetail(serviceId)
                if (info == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Không tìm thấy dịch vụ"
                        )
                    }
                    return@launch
                }
                val packages = repository.getPackages(serviceId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        serviceInfo = info,
                        packages = packages,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Không thể tải dữ liệu"
                    )
                }
            }
        }
    }

    fun onAddToCart(packageId: String) {
        // TODO: gọi CartRepository khi có
        _uiState.update { it.copy(showAddToCartSuccess = true) }
    }

    fun dismissAddToCartDialog() {
        _uiState.update { it.copy(showAddToCartSuccess = false) }
    }

    fun onBottomTabSelected(index: Int) {
        _uiState.update { it.copy(selectedBottomTab = index) }
    }

    companion object {
        fun provideFactory(
            serviceId: String,
            repository: ServicesRepository = FakeServicesRepository()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ServiceDetailViewModel(repository, serviceId) as T
            }
        }
    }
}
