package com.example.bmis.ui.screens.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
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


// Data class cho Tab 1 (Danh sách dịch vụ)
data class UtilityService(
    val id: Int,
    val title: String,
    val description: String,
    val registeredCount: Int,
    val icon: ImageVector,
    val iconColor: Color
)

// Data class cho Tab 2 (Danh sách đăng ký)
data class RegistrationStatus(
    val id: Int,
    val title: String,
    val countText: String,
    val statusType: StatusType
)

enum class StatusType {
    SUCCESS, WARNING, ERROR
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(
    initialTabIndex: Int = 0,
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onStatusClick: (RegistrationStatus) -> Unit = {}
) {
    var selectedTabIndex by remember { mutableIntStateOf(initialTabIndex) } // Mặc định mở Tab 0 (Danh sách dịch vụ)
    val tabs = listOf("Danh sách dịch vụ", "Danh sách đăng ký")

    // Dữ liệu giả lập Tab 1
    val sampleServices = listOf(
        UtilityService(1, "Dịch vụ GYM", "Cung cấp dịch vụ GYM", 250, Icons.Default.FitnessCenter, PrimaryBlue),
        UtilityService(2, "Dịch vụ Bơi lội", "Cung cấp dịch vụ Bơi lội", 250, Icons.Default.Pool, PrimaryBlue)
    )

    // Dữ liệu giả lập Tab 2 (Đúng theo UI mới gửi)
    val sampleStatuses = listOf(
        RegistrationStatus(1, "Đã thanh toán", "2 dịch vụ tiện ích", StatusType.SUCCESS),
        RegistrationStatus(2, "Chưa thanh toán", "3 dịch vụ tiện ích", StatusType.WARNING),
        RegistrationStatus(3, "Đã hủy", "2 dịch vụ tiện ích", StatusType.ERROR)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Dịch vụ tiện ích", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Tab Row
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.White,
                contentColor = PrimaryBlue,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = OrangeHighlight
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontWeight = if (selectedTabIndex == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabIndex == index) PrimaryBlue else TextGray,
                                fontSize = 14.sp
                            )
                        }
                    )
                }
            }

            // Xử lý hiển thị nội dung theo từng Tab
            when (selectedTabIndex) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(sampleServices) { service ->
                            ServiceItemCard(service = service)
                        }
                    }
                }
                1 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(sampleStatuses) { status ->
                            StatusItemCard(status = status, onClick = { onStatusClick(status) })
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun StatusItemCard(
    status: RegistrationStatus,
    onClick: () -> Unit
) {
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
                // Icon ví/thẻ nền xanh nhạt chính diện
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F4FA))
                        .align(Alignment.Center),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.CreditCard,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(26.dp)
                    )
                }

                // Badge nhỏ hiển thị trạng thái (Góc dưới bên phải)
                val (badgeIcon, badgeColor) = when (status.statusType) {
                    StatusType.SUCCESS -> Icons.Default.CheckCircle to Color(0xFF4CAF50)  // Xanh lá
                    StatusType.WARNING -> Icons.Default.Warning to Color(0xFFFF9800)      // Cam chỉ dẫn
                    StatusType.ERROR -> Icons.Default.Cancel to Color(0xFFF44336)         // Đỏ hủy bỏ
                }

                Icon(
                    imageVector = badgeIcon,
                    contentDescription = null,
                    tint = badgeColor,
                    modifier = Modifier
                        .size(18.dp)
                        .background(Color.White, CircleShape) // Viền trắng bo tròn bao quanh badge giống mẫu
                        .align(Alignment.BottomEnd)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Phần text hiển thị thông tin trạng thái
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = status.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = status.countText,
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
private fun ServiceItemCard(service: UtilityService) {
    Card(
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
            // Icon service
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF0F4FA)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = service.icon,
                    contentDescription = null,
                    tint = service.iconColor,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Thông tin service
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = service.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = service.description,
                    fontSize = 13.sp,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.People,
                        contentDescription = null,
                        tint = OrangeHighlight,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${service.registeredCount} người đã đăng ký",
                        fontSize = 12.sp,
                        color = OrangeHighlight,
                        fontWeight = FontWeight.Medium
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
        ServicesScreen(initialTabIndex = 1)
    }
}

@Preview(showBackground = true, name = "Danh sách dịch vụ (Tab 1)")
@Composable
fun ServicesScreenListTabPreview() {
    BMISTheme {
        ServicesScreen(initialTabIndex = 0)
    }
}