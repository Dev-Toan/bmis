package com.example.bmis.ui.screens.LuongSuaChua.DichVuSuaChua

import androidx.compose.foundation.lazy.items


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmis.ui.theme.BMISTheme
import com.example.bmis.ui.theme.BackgroundColor
import com.example.bmis.ui.theme.OrangeHighlight
import com.example.bmis.ui.theme.PrimaryBlue
import com.example.bmis.ui.theme.TextGray
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.data.models.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DichVuSuaChuaScreen(
    initialTabIndex: Int = 0,
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onStatusClick: (RepairRequest) -> Unit = {},
    onServiceClick: (RepairCategory) -> Unit = {},
    viewModel: DanhSachDichVuSuaChuaViewModel = viewModel(factory = DanhSachDichVuSuaChuaViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by rememberSaveable { mutableIntStateOf(initialTabIndex) }

    val tabs = listOf("Danh sách dịch vụ", "Danh sách đăng ký")
    val services = uiState.services
    val statuses = listOf(
        RepairStatus.PENDING,
        RepairStatus.RECEIVED,
        RepairStatus.PROCESSED
    ).map { status ->
        RepairRequest(
            id = "",
            registrationCode = "",
            serviceName = "",
            apartment = "",
            registrationDate = "",
            status = status
        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dịch vụ sửa chữa", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextGray)
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = TextGray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                tabs.forEachIndexed { index, title ->
                    val isSelected = selectedTab == index
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clickable { selectedTab = index }
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = title,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) PrimaryBlue else TextGray,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .height(3.dp)
                                .fillMaxWidth(0.7f)
                                .background(
                                    color = if (isSelected) OrangeHighlight else Color.Transparent,
                                    shape = RoundedCornerShape(999.dp)
                                )
                        )
                    }
                }
            }

            // Xử lý hiển thị nội dung theo từng Tab
            when (selectedTab) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(services) { service ->
                            ServiceItemCard(service = service, onClick = {
                                viewModel.onServiceClicked(service)
                                onServiceClick(service)
                            })
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(statuses) { status ->
                            StatusItemCard(status = status, onClick = {
                                viewModel.onStatusClicked(status)
                                onStatusClick(status)
                            })
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun StatusItemCard(
    status: RepairRequest,
    onClick: () -> Unit
) {
    // Phân tích trạng thái để cấu hình Tiêu đề, Mô tả, Icon badge và Màu sắc
    val title: String
    val description: String // Trong thực tế bạn có thể thay số lượng bằng biến: "${status.count} yêu cầu sửa chữa"
    val badgeIcon: ImageVector
    val badgeColor: Color

    when (status.status) {
        RepairStatus.PENDING -> {
            title = "Chưa tiếp nhận"
            description = "2 yêu cầu sửa chữa"
            badgeIcon = Icons.Default.Error // Dấu chấm than
            badgeColor = Color(0xFFF44336)  // Đỏ
        }
        RepairStatus.RECEIVED -> {
            title = "Đã tiếp nhận"
            description = "4 yêu cầu sửa chữa"
            badgeIcon = Icons.Default.Refresh // Dấu vòng lặp/xoay
            badgeColor = Color(0xFFFF9800)    // Cam
        }
        RepairStatus.PROCESSED -> {
            title = "Đã xử lý"
            description = "2 yêu cầu sửa chữa"
            badgeIcon = Icons.Default.CheckCircle // Dấu tích tròn
            badgeColor = Color(0xFF4CAF50)        // Xanh lá
        }
    }

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Cụm Icon tròn kèm Badge góc dưới bên phải
            Box(
                modifier = Modifier.size(56.dp)
            ) {
                // Icon bánh răng nền xanh nhạt chính diện
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F4FA))
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings, // Đổi thành Settings (Bánh răng)
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Badge nhỏ hiển thị trạng thái (Góc dưới bên phải)
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(20.dp)
                        .background(Color.White, CircleShape), // Nền trắng để tạo viền cắt
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = badgeIcon,
                        contentDescription = null,
                        tint = badgeColor,
                        modifier = Modifier.size(16.dp) // Kích thước icon nhỏ hơn box để lộ viền trắng
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Phần text hiển thị thông tin trạng thái
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    fontSize = 13.sp,
                    color = TextGray
                )
            }

            // Mũi tên chỉ hướng dịch chuyển sang phải (>)
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = "Go to detail",
                tint = TextGray,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

// Giữ nguyên Card cũ của Tab 1 ở dưới nếu bạn cần...
@Composable
private fun ServiceItemCard(service: RepairCategory, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Icon service
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF0F4FA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = service.icon ?: Icons.Default.Settings,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin service
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = service.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = service.description,
                    fontSize = 13.sp,
                    color = TextGray,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Nút Đăng ký
                Button(
                    onClick = onClick,
                    modifier = Modifier.height(32.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeHighlight),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "Đăng ký",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Danh sách đăng ký (Tab 2)")
@Composable
fun ServicesScreenRegistrationTabPreview() {
    BMISTheme {
        // Preview cho cấu hình mặc định hiện tại của bạn (selectedTabIndex = 1)
        DichVuSuaChuaScreen(initialTabIndex = 1)
    }
}

@Preview(showBackground = true, name = "Danh sách dịch vụ (Tab 1)")
@Composable
fun ServicesScreenListTabPreview() {
    BMISTheme {
        DichVuSuaChuaScreen(initialTabIndex = 0)
    }
}
