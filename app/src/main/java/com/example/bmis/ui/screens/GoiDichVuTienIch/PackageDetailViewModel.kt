package com.example.bmis.ui.screens.GoiDichVuTienIch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.models.PackageDetail
import com.example.bmis.data.repository.FakeUtilityRepository
import com.example.bmis.data.repository.UtilityRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PackageDetailUiState(
    val isLoading: Boolean = false,
    val packageDetail: PackageDetail? = null,
    val showAddToCartSuccess: Boolean = false,
    val errorMessage: String? = null
)

class PackageDetailViewModel(
    private val repository: UtilityRepository,
    private val serviceId: String,
    private val packageId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(PackageDetailUiState(isLoading = true))
    val uiState: StateFlow<PackageDetailUiState> = _uiState.asStateFlow()

    init {
        loadPackageDetail()
    }

    fun loadPackageDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val detail = repository.getPackageDetail(serviceId, packageId)
                if (detail == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Không tìm thấy gói dịch vụ"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, packageDetail = detail, errorMessage = null)
                    }
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

    fun onAddToCart() {
        // TODO: gọi CartRepository khi tích hợp giỏ hàng
        _uiState.update { it.copy(showAddToCartSuccess = true) }
    }

    fun dismissAddToCartDialog() {
        _uiState.update { it.copy(showAddToCartSuccess = false) }
    }

    companion object {
        fun provideFactory(
            serviceId: String,
            packageId: String,
            repository: UtilityRepository = FakeUtilityRepository()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PackageDetailViewModel(repository, serviceId, packageId) as T
            }
        }
    }
}
