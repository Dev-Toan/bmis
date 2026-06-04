package com.example.bmis.ui.screens.LuongDongGopYKien.ChiTietYKien

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.bmis.data.models.Comment
import com.example.bmis.data.models.FeedbackDetail
import com.example.bmis.data.models.FeedbackStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class ChiTietYKienUiState(
    val feedback: FeedbackDetail? = null,
    val isLoading: Boolean = false,
    val messageText: String = "",
    val selectedImages: List<Uri> = emptyList(),
    val showStaffInfo: Boolean = false,
    val showImagePicker: Boolean = false
)

class ChiTietYKienViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ChiTietYKienUiState())
    val uiState: StateFlow<ChiTietYKienUiState> = _uiState.asStateFlow()

    init {
        loadDetail()
    }

    private fun loadDetail() {
        _uiState.update { it.copy(isLoading = true) }
        // Giả lập dữ liệu từ hình ảnh
        val mockFeedback = FeedbackDetail(
            id = "1",
            title = "Gây mất trật tự",
            status = FeedbackStatus.PROCESSING,
            apartment = "1307",
            type = "Khiếu nại",
            content = "Căn hộ 1307 hát karaoke",
            attachedImages = listOf("uri1", "uri2"),
            comments = listOf(
                Comment(
                    id = "c1",
                    userName = "Lê Thanh Cường",
                    content = "Cảm ơn quý cư dân đã gửi ý kiến, ban quản lý sẽ xem xét và liên hệ ngay với căn hộ trên để xử lý và thông báo lại quý cư dân sau khi có kết quả.",
                    timestamp = "22/10/2019 | 14:38:20",
                    isStaff = true
                ),
                Comment(
                    id = "c2",
                    userName = "Phạm Tuấn Anh",
                    content = "Cảm ơn BQL đã xử lý kịp thời",
                    timestamp = "22/10/2019 | 14:38:20",
                    isStaff = false
                ),
                Comment(
                    id = "c3",
                    userName = "Phạm Tuấn Anh",
                    content = "Tôi mới dọn vào căn hộ 1308 nên chưa nắm rõ các quy định.",
                    timestamp = "22/10/2019 | 14:38:20",
                    isStaff = false,
                    attachedImages = listOf("img_karaoke")
                )
            )
        )
        _uiState.update { it.copy(feedback = mockFeedback, isLoading = false) }
    }

    fun onMessageChange(text: String) {
        _uiState.update { it.copy(messageText = text) }
    }

    fun onAddImages(uris: List<Uri>) {
        _uiState.update { state ->
            state.copy(selectedImages = state.selectedImages + uris)
        }
    }

    fun removeImage(uri: Uri) {
        _uiState.update { state ->
            state.copy(selectedImages = state.selectedImages.filter { it != uri })
        }
    }

    fun sendMessage() {
        // Logic gửi tin nhắn
        _uiState.update { it.copy(messageText = "", selectedImages = emptyList()) }
    }

    fun toggleStaffInfo(show: Boolean) {
        _uiState.update { it.copy(showStaffInfo = show) }
    }

    fun toggleImagePicker(show: Boolean) {
        _uiState.update { it.copy(showImagePicker = show) }
    }
}
