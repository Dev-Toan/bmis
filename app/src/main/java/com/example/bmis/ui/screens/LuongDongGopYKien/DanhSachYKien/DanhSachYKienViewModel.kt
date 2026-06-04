package com.example.bmis.ui.screens.LuongDongGopYKien.DanhSachYKien

import androidx.lifecycle.ViewModel
import com.example.bmis.data.models.FeedbackItem
import com.example.bmis.data.models.FeedbackStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class FeedbackUiState(
    val feedbackList: List<FeedbackItem> = emptyList(),
    val isLoading: Boolean = false
)

class DanhSachYKienViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    init {
        loadFeedbacks()
    }

    private fun loadFeedbacks() {
        _uiState.value = FeedbackUiState(
            feedbackList = listOf(
                FeedbackItem(
                    id = "1",
                    title = "Gây mất trật tự",
                    apartment = "1307",
                    type = "Khiếu nại",
                    content = "Căn hộ 1307 hát karaoke",
                    status = FeedbackStatus.WAITING,
                    date = "29/09/2025"
                ),
                FeedbackItem(
                    id = "2",
                    title = "Gây mất trật tự",
                    apartment = "1307",
                    type = "Khiếu nại",
                    content = "Căn hộ 1307 hát karaoke",
                    status = FeedbackStatus.PROCESSING,
                    date = "29/09/2025"
                ),
                FeedbackItem(
                    id = "3",
                    title = "Gây mất trật tự",
                    apartment = "1307",
                    type = "Khiếu nại",
                    content = "Căn hộ 1307 hát karaoke",
                    status = FeedbackStatus.RESOLVED,
                    date = "29/09/2025"
                )
            )
        )
    }
}
