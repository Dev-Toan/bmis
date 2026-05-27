package com.example.bmis.ui.screens.ChiTietDanhSachDangKy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.models.ItemsServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// 1. Định nghĩa trạng thái UI
data class PaidServicesUiState(
    val isLoading: Boolean = false,
    val invoices: List<ItemsServices> = emptyList(),
    val errorMessage: String? = null
)

class ItemsServicesViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PaidServicesUiState())
    val uiState: StateFlow<PaidServicesUiState> = _uiState.asStateFlow()

    init {
        loadInvoices()
    }

    fun loadInvoices() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            // Trong thực tế, bạn sẽ gọi Repository ở đây
            // Đây là dữ liệu giả lập (Mock Data)
            val mockData = List(2) {
                ItemsServices(
                    id = it.toString(),
                    registrationCode = "DK0000$it",
                    servicesName = "Dịch vụ GYM, Dịch vụ bơi lội",
                    registrationDate = "21/09/2019",
                    paymentDate = "21/09/2019",
                    totalAmount = "810.000 VNĐ"
                )
            }
            _uiState.update { it.copy(isLoading = false, invoices = mockData) }
        }
    }
}