package com.example.bmis.ui.screens.ChiTietDichVu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.bmis.R
import com.example.bmis.ui.theme.BMISTheme

private val PrimaryBlue = Color(0xFF0F3C88)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)
private val DiscountRed = Color(0xFFFF4D4D)

// Data class đại diện cho thông tin gói dịch vụ
data class ServicePackage(
    val id: String,
    val title: String,
    val registrationCount: Int,
    val currentPrice: String,
    val originalPrice: String,
    val discountPercent: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onAddToCartClick: (ServicePackage) -> Unit = {},
    onViewPackageClick: (ServicePackage) -> Unit = {}
) {
    // Dữ liệu giả lập 2 gói cước giống trong ảnh
    val samplePackages = listOf(
        ServicePackage("1", "Gói cơ bản 1 tháng", 100, "750.000 VNĐ", "1.000.000 VNĐ", "-25%"),
        ServicePackage("2", "Gói cơ bản 1 tháng", 100, "750.000 VNĐ", "1.000.000 VNĐ", "-25%")
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // 1. Phần Banner Header lớn phía trên cùng
            item {
                ServiceHeaderBanner()
            }

            // 2. Tiêu đề phân mục "Danh sách gói (2)"
            item {
                Text(
                    text = "Danh sách gói (${samplePackages.size})",
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 12.dp)
                )
            }

            // 3. Danh sách các gói cước tiện ích
            items(samplePackages) { item ->
                PackageItemCard(
                    servicePackage = item,
                    onAddToCart = { onAddToCartClick(item) },
                    onViewPackage = { onViewPackageClick(item) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun ServiceHeaderBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        // Hình nền phòng GYM (Bạn thay R.drawable.gym_banner bằng ảnh thực tế của bạn)
        Image(
            painter = painterResource(id = R.drawable.gym),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Lớp phủ đen mờ để làm nổi bật chữ trắng
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        // Cụm thông tin dịch vụ xếp đè lên banner
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Icon tròn biểu tượng dịch vụ
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Nội dung Text trắng
                Column {
                    Text(
                        text = "Dịch vụ GYM",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "Cung cấp dịch vụ GYM",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                }

            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "250 Người đã đăng ký",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        // Badge chỉ số trang nhỏ ở góc phải "1/5"
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .background(PrimaryBlue.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(text = "1/5", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
private fun PackageItemCard(
    servicePackage: ServicePackage,
    onAddToCart: () -> Unit,
    onViewPackage: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Khối trên: Thông tin giá cả và nút thêm giỏ hàng
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Bên trái: Tên gói & Giá tiền
                Column {
                    Text(text = servicePackage.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                    Text(text = "${servicePackage.registrationCount} lượt đăng ký", color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.Medium)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = servicePackage.currentPrice, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = servicePackage.discountPercent, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DiscountRed)
                    }
                    Text(
                        text = servicePackage.originalPrice,
                        color = TextGray,
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                // Bên phải: Nút "Thêm vào giỏ" vuông viền mảnh bo tròn
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(12.dp))
                        .clickable { onAddToCart() }
                        .padding(horizontal = 12.dp, vertical = 10.dp)
                ) {
                    Icon(imageVector = Icons.Default.AddShoppingCart, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Thêm vào giỏ", fontSize = 10.sp, color = Color.Black, fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Khối dưới: Nút "Xem gói" to màu xanh bo tròn rộng hết cỡ
            Button(
                onClick = onViewPackage,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(22.dp)
            ) {
                Text(text = "Xem gói", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Preview(showBackground = true, name = "Chi tiết gói dịch vụ GYM")
@Composable
fun ServiceDetailScreenPreview() {
    BMISTheme {
        ServiceDetailScreen()
    }
}