package com.example.bmis.ui.screens.LuongDongGopYKien.TaoYKienMoi

import android.net.Uri
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class TaoYKienMoiUiState(
    val title: String = "",
    val apartment: String = "1307",
    val feedbackType: String = "Kiến nghị",
    val imageUri: Uri? = null,
    val content: String = "",
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

class TaoYKienMoiViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TaoYKienMoiUiState())
    val uiState: StateFlow<TaoYKienMoiUiState> = _uiState.asStateFlow()

    fun onTitleChange(newTitle: String) {
        _uiState.update { it.copy(title = newTitle) }
    }

    fun onContentChange(newContent: String) {
        _uiState.update { it.copy(content = newContent) }
    }

    fun onImageSelected(uri: Uri?) {
        _uiState.update { it.copy(imageUri = uri) }
    }

    fun submitFeedback() {
        _uiState.update { it.copy(isSubmitting = true) }
        // Giả lập gửi dữ liệu
        _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }
    }

    fun resetSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }
}
