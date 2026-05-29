package com.example.bmis.ui.screens.ThongTinDangKy

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.bmis.data.models.RegistrationDetail
import com.example.bmis.data.models.RegistrationOrderStatus
import com.example.bmis.data.repository.FakeRegistrationRepository
import com.example.bmis.data.repository.RegistrationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationDetailUiState(
    val isLoading: Boolean = false,
    val detail: RegistrationDetail? = null,
    val showEmployeeDialog: Boolean = false,
    val errorMessage: String? = null
)

class RegistrationDetailViewModel(
    private val repository: RegistrationRepository,
    private val registrationId: String,
    private val status: RegistrationOrderStatus
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegistrationDetailUiState(isLoading = true))
    val uiState: StateFlow<RegistrationDetailUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    fun loadDetail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val detail = repository.getRegistrationDetail(registrationId, status)
                if (detail == null) {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = "Không tìm thấy thông tin đăng ký"
                        )
                    }
                } else {
                    _uiState.update {
                        it.copy(isLoading = false, detail = detail, errorMessage = null)
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

    fun showEmployeeDialog() {
        _uiState.update { it.copy(showEmployeeDialog = true) }
    }

    fun dismissEmployeeDialog() {
        _uiState.update { it.copy(showEmployeeDialog = false) }
    }

    companion object {
        fun provideFactory(
            registrationId: String,
            status: RegistrationOrderStatus,
            repository: RegistrationRepository = FakeRegistrationRepository()
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RegistrationDetailViewModel(repository, registrationId, status) as T
            }
        }
    }
}
