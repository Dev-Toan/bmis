package com.example.bmis.ui.screens.LuongSuaChua.DangKyDichVu

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DangKyDichVuUiState(
    val description: String = "",
    val imageUri: Uri? = null,
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class DangKyDichVuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(DangKyDichVuUiState())
    val uiState: StateFlow<DangKyDichVuUiState> = _uiState.asStateFlow()

    fun onDescriptionChange(newDescription: String) {
        _uiState.update { it.copy(description = newDescription) }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun submitRegistration() {
        // Logic gửi đăng ký lên server (giả lập)
        _uiState.update { it.copy(isSubmitting = true) }
        // Sau khi thành công:
        _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
