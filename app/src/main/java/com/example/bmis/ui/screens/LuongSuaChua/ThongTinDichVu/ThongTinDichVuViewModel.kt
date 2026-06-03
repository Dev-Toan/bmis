package com.example.bmis.ui.screens.LuongSuaChua.ThongTinDichVu

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class ThongTinDichVuUiState(
    val title: String = "Sửa chữa Điện lạnh",
    val description: String = "Điện lạnh là những thiết bị thiết yếu mang lại sự tiện nghi cho cuộc sống hiện đại. Tuy nhiên, chúng có thể gặp phải sự cố bất ngờ. Đừng lo lắng! Dịch vụ sửa chữa điện lạnh chuyên nghiệp của chúng tôi sẽ là giải pháp toàn diện, nhanh chóng và đáng tin cậy cho mọi vấn đề của gia đình bạn.",
    val devicesTitle: String = "Chúng tôi chuyên sửa chữa các loại thiết bị như:",
    val devices: List<String> = listOf(
        "Máy lạnh/Điều hòa: Không mát, chảy nước, kêu to, báo lỗi...",
        "Tủ lạnh/Tủ đông: Không đông đá, kém lạnh, rò rỉ gas, hỏng block...",
        "Máy giặt: Không giặt, không vắt, tràn nước, hư hỏng bo mạch...",
        "Bình nóng lạnh/Máy nước nóng: Không nóng, rò điện, hỏng rơ le..."
    ),
    val commitmentTitle: String = "Với đội ngũ kỹ thuật viên lành nghề, giàu kinh nghiệm và trang bị công cụ hiện đại, chúng tôi cam kết:",
    val commitments: List<String> = listOf(
        "Kiểm tra tận nơi, báo giá rõ ràng trước khi tiến hành sửa chữa.",
        "Sửa chữa dứt điểm, nhanh chóng, khôi phục hiệu suất tối đa cho thiết bị.",
        "Sử dụng linh kiện chính hãng có bảo hành."
    )
)

class ThongTinDichVuViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ThongTinDichVuUiState())
    val uiState: StateFlow<ThongTinDichVuUiState> = _uiState.asStateFlow()
}
