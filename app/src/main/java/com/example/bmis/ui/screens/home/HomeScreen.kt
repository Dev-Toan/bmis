package com.example.bmis.ui.screens.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.R
import com.example.bmis.data.models.Post
import com.example.bmis.ui.theme.BMISTheme

val PrimaryBlue = Color(0xFF0F3C88)
val BackgroundColor = Color(0xFFF4F6F9)
val TextGray = Color(0xFF8A92A6)

@Composable
fun HomeScreen(
    userName: String = "",
    onNewsClick: (Post) -> Unit = {},
    onServicesClick: () -> Unit = {},
    onRepairClick: () -> Unit = {},
    onFeedbackClick: () -> Unit = {},
    homeViewModel: HomeViewModel? = null,
    previewUiState: HomeUiState? = null
) {
    val uiState = previewUiState ?: run {
        val resolvedViewModel = homeViewModel ?: viewModel(factory = HomeViewModel.Factory)
        resolvedViewModel.uiState.collectAsState().value
    }

    Scaffold(
//        bottomBar = { BottomNavigationBar() },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            HeaderSection(userName = userName)
            BannerSection()
            MenuGridSection(
                onServicesClick = onServicesClick,
                onRepairClick = onRepairClick,
                onFeedbackClick = onFeedbackClick
            )

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else {
                NewsSection(posts = uiState.newsList, onNewsClick = onNewsClick)
            }

            if (uiState.errorMessage != null) {
                Text(
                    text = "Lỗi: ${uiState.errorMessage}",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@Composable
private fun HeaderSection(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = "Xin chào!", color = Color.Gray, fontSize = 14.sp)
            Text(
                text = userName,
                color = PrimaryBlue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Notifications",
                tint = Color.Gray,
                modifier = Modifier.size(28.dp)
            )
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(Color.Red)
                    .align(Alignment.TopEnd)
            )
        }
    }
}

@Composable
private fun BannerSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        Image(
            painter = painterResource(id = R.drawable.banner),
            contentDescription = "Banner",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
private fun MenuGridSection(
    onServicesClick: () -> Unit = {},
    onPaymentClick: () -> Unit = {},
    onRepairClick: () -> Unit = {},
    onEmergencyClick: () -> Unit = {},
    onFeedbackClick: () -> Unit = {},
    onHotlineClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        // Đặt 2 Row nằm trực tiếp trong Column để xếp theo chiều dọc
        Column(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp) // Khoảng cách giữa hàng 1 và hàng 2
        ) {
            // Hàng 1: Thanh toán, Tiện ích, Sửa chữa
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MenuItem(
                    icon = Icons.Default.CreditCard,
                    title = "Thanh toán",
                    iconBgColor = Color(0xFFFF9800),
                    iconColor = Color.White,
                    onClick = onPaymentClick
                )
                MenuItem(
                    icon = Icons.Default.Dashboard,
                    title = "Tiện ích",
                    iconBgColor = Color(0xFFF0F4FA),
                    iconColor = PrimaryBlue,
                    onClick = onServicesClick
                )
                MenuItem(
                    icon = Icons.Default.Build,
                    title = "Sửa chữa",
                    iconBgColor = Color(0xFFF0F4FA),
                    iconColor = PrimaryBlue,
                    onClick = onRepairClick
                )
            }

            // Hàng 2: Khẩn cấp, Đóng góp, Hotline
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MenuItem(
                    icon = Icons.Default.NotificationsActive,
                    title = "Khẩn cấp",
                    iconBgColor = Color(0xFFF0F4FA),
                    iconColor = PrimaryBlue,
                    onClick = onEmergencyClick
                )
                MenuItem(
                    icon = Icons.Default.Email,
                    title = "Đóng góp",
                    iconBgColor = Color(0xFFF0F4FA),
                    iconColor = PrimaryBlue,
                    onClick = onFeedbackClick
                )
                MenuItem(
                    icon = Icons.Default.Call,
                    title = "Hotline",
                    iconBgColor = Color(0xFFF0F4FA),
                    iconColor = PrimaryBlue,
                    onClick = onHotlineClick
                )
            }
        }
    }
}

@Composable
private fun MenuItem(
    icon: ImageVector,
    title: String,
    iconBgColor: Color,
    iconColor: Color,
    onClick: () -> Unit = {}
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(80.dp)
    ) {
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = iconBgColor),
            modifier = Modifier.size(56.dp)
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconColor,
                    modifier = Modifier.size(28.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 12.sp,
            color = PrimaryBlue,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun NewsSection(posts: List<Post> = emptyList(), onNewsClick: (Post) -> Unit = {}) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Text(
            text = "Tin tức - Hoạt động",
            color = PrimaryBlue,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        posts.take(10).forEach { post ->
            NewsCard(
                title = post.title,
                description = post.body,
                imageRes = R.drawable.chuachay,
                onClick = { onNewsClick(post) }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun NewsCard(title: String, description: String, imageRes: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            )

            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = TextGray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

private fun sampleHomeUiState() = HomeUiState(
    newsList = listOf(
        Post(
            userId = 1,
            id = 1,
            title = "Diễn tập phòng cháy chữa cháy tại khu dân cư",
            body = "Tổng hợp hình ảnh và nội dung buổi diễn tập phòng cháy chữa cháy mới nhất."
        ),
        Post(
            userId = 1,
            id = 2,
            title = "Thông báo lịch bảo trì hệ thống nước",
            body = "Kế hoạch bảo trì hệ thống nước sẽ được thực hiện trong tuần này."
        )
    )
)

//@Composable
//private fun BottomNavigationBar() {
//    NavigationBar(
//        containerColor = Color.White,
//        tonalElevation = 8.dp
//    ) {
//        NavigationBarItem(
//            selected = true,
//            onClick = { },
//            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
//            label = { Text("Trang chủ") },
//            colors = NavigationBarItemDefaults.colors(
//                selectedIconColor = PrimaryBlue,
//                selectedTextColor = PrimaryBlue,
//                unselectedIconColor = Color.Gray,
//                unselectedTextColor = Color.Gray,
//                indicatorColor = Color.White
//            )
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { },
//            icon = { Icon(Icons.Default.Group, contentDescription = "Members") },
//            label = { Text("Thành viên") },
//            colors = NavigationBarItemDefaults.colors(
//                unselectedIconColor = Color.Gray,
//                unselectedTextColor = Color.Gray
//            )
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { },
//            icon = { Icon(Icons.Default.AccountBalanceWallet, contentDescription = "Pay") },
//            label = { Text("Pay") },
//            colors = NavigationBarItemDefaults.colors(
//                unselectedIconColor = Color.Gray,
//                unselectedTextColor = Color.Gray
//            )
//        )
//        NavigationBarItem(
//            selected = false,
//            onClick = { },
//            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
//            label = { Text("Cá nhân") },
//            colors = NavigationBarItemDefaults.colors(
//                unselectedIconColor = Color.Gray,
//                unselectedTextColor = Color.Gray
//            )
//        )
//    }
//}

@Preview(showBackground = true, name = "Giao diện chính - Light Mode")
@Composable
fun HomeScreenPreview() {
    BMISTheme {
        HomeScreen(
            userName = "Nguyễn Văn A",
            onNewsClick = {},
            onServicesClick = {},
            previewUiState = sampleHomeUiState()
        )
    }
}