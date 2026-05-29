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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.R
import com.example.bmis.data.models.ServiceDetailInfo
import com.example.bmis.data.models.ServicePackage
import com.example.bmis.ui.theme.BMISTheme
import androidx.compose.ui.graphics.vector.ImageVector

private val PrimaryBlue = Color(0xFF0F3C88)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)
private val DiscountRed = Color(0xFFFF4D4D)
private val SuccessOrange = Color(0xFFFF7A00) // Màu cam của dấu tick trong ảnh


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServiceDetailScreen(
    serviceId: String = "1",
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onViewPackageClick: (ServicePackage) -> Unit = {},
    viewModel: ServiceDetailViewModel = viewModel(
        factory = ServiceDetailViewModel.provideFactory(serviceId)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
//        bottomBar = {
//            // Cải tiến 1: Thêm Bottom Navigation Bar giống thiết kế
//            NavigationBar(
//                containerColor = Color.White,
//                tonalElevation = 8.dp
//            ) {
//                NavigationBarItem(
//                    selected = uiState.selectedBottomTab == 0,
//                    onClick = { viewModel.onBottomTabSelected(0) },
//                    icon = { Icon(Icons.Default.Home, contentDescription = "Trang chủ") },
//                    label = { Text("Trang chủ", fontSize = 11.sp) },
//                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue, unselectedIconColor = TextGray, unselectedTextColor = TextGray)
//                )
//                NavigationBarItem(
//                    selected = uiState.selectedBottomTab == 1,
//                    onClick = { viewModel.onBottomTabSelected(1) },
//                    icon = { Icon(Icons.Default.Group, contentDescription = "Thành viên") },
//                    label = { Text("Thành viên", fontSize = 11.sp) },
//                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue, unselectedIconColor = TextGray, unselectedTextColor = TextGray)
//                )
//                NavigationBarItem(
//                    selected = uiState.selectedBottomTab == 2,
//                    onClick = { viewModel.onBottomTabSelected(2) },
//                    icon = { Icon(Icons.Default.CreditCard, contentDescription = "Pay") },
//                    label = { Text("Pay", fontSize = 11.sp) },
//                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue, unselectedIconColor = TextGray, unselectedTextColor = TextGray)
//                )
//                NavigationBarItem(
//                    selected = uiState.selectedBottomTab == 3,
//                    onClick = { viewModel.onBottomTabSelected(3) },
//                    icon = { Icon(Icons.Default.Person, contentDescription = "Cá nhân") },
//                    label = { Text("Cá nhân", fontSize = 11.sp) },
//                    colors = NavigationBarItemDefaults.colors(selectedIconColor = PrimaryBlue, selectedTextColor = PrimaryBlue, unselectedIconColor = TextGray, unselectedTextColor = TextGray)
//                )
//            }
//        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = PrimaryBlue)
                    }
                }
                uiState.errorMessage != null -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "",
                            color = TextGray,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        uiState.serviceInfo?.let { info ->
                            item { ServiceHeaderBanner(serviceInfo = info) }
                        }

                        item {
                            Text(
                                text = "Danh sách gói (${uiState.packages.size})",
                                color = PrimaryBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.padding(start = 16.dp, top = 20.dp, bottom = 12.dp)
                            )
                        }

                        items(uiState.packages, key = { it.id }) { item ->
                            PackageItemCard(
                                servicePackage = item,
                                onAddToCart = { viewModel.onAddToCart(item.id) },
                                onViewPackage = { onViewPackageClick(item) }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            if (uiState.showAddToCartSuccess) {
                AddToCartSuccessDialog(onDismiss = { viewModel.dismissAddToCartDialog() })
            }
        }
    }
}

@Composable
private fun AddToCartSuccessDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 40.dp, horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Biểu tượng dấu tick chữ V màu cam nét dày
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Success",
                    tint = SuccessOrange,
                    modifier = Modifier
                        .size(80.dp)
                        .border(4.dp, SuccessOrange, CircleShape)
                        .padding(12.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "Thêm vào giỏ thành công",
                    color = Color(0xFF1A1D23),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

private fun serviceIconFor(iconType: String): ImageVector = when (iconType) {
    "pool" -> Icons.Default.Pool
    else -> Icons.Default.FitnessCenter
}

private fun bannerDrawableFor(resName: String): Int = when (resName) {
    "gym" -> R.drawable.gym
    else -> R.drawable.gym
}

@Composable
private fun ServiceHeaderBanner(serviceInfo: ServiceDetailInfo) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
    ) {
        Image(
            painter = painterResource(id = bannerDrawableFor(serviceInfo.bannerImageResName)),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.4f))
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomStart)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = serviceIconFor(serviceInfo.iconType),
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(30.dp)
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = serviceInfo.title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = serviceInfo.description,
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Group,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.8f),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${serviceInfo.registeredCount} Người đã đăng ký",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(12.dp)
                .background(PrimaryBlue.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                .padding(horizontal = 8.dp, vertical = 2.dp)
        ) {
            Text(
                text = serviceInfo.galleryIndex,
                color = Color.White,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
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
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(text = servicePackage.title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
                    Text(text = "${servicePackage.registrationCount} lượt đăng ký", color = PrimaryBlue, fontSize = 12.sp, fontWeight = FontWeight.Medium)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = servicePackage.currentPrice, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(text = servicePackage.discountPercent, fontWeight = FontWeight.Bold, fontSize = 11.sp, color = DiscountRed)
                    }
                    Text(text = servicePackage.originalPrice, color = TextGray, fontSize = 12.sp, textDecoration = TextDecoration.LineThrough)
                }

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
        ServiceDetailScreen(serviceId = "1")
    }
}