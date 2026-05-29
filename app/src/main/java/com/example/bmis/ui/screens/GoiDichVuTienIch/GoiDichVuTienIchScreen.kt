package com.example.bmis.ui.screens.GoiDichVuTienIch

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.data.models.PackageDetail
import com.example.bmis.ui.theme.BMISTheme

private val PrimaryBlue = Color(0xFF0F3C88)
private val LightBlueBorder = Color(0xFF3EA3FF)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)
private val DiscountRed = Color(0xFFFF4D4D)
private val OrangeButton = Color(0xFFFF912C)
private val SuccessOrange = Color(0xFFFF7A00)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PackageDetailScreen(
    serviceId: String = "1",
    packageId: String = "1",
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onAddToCartClick: () -> Unit = {},
    viewModel: PackageDetailViewModel = viewModel(
        factory = PackageDetailViewModel.provideFactory(serviceId, packageId)
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Gói dịch vụ tiện ích",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextGray
                        )
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
        bottomBar = {
            uiState.packageDetail?.let {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    Button(
                        onClick = {
                            viewModel.onAddToCart()
                            onAddToCartClick()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = OrangeButton),
                        shape = RoundedCornerShape(27.dp)
                    ) {
                        Text(
                            text = "Thêm vào giỏ hàng",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        },
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
                uiState.packageDetail != null -> {
                    PackageDetailContent(
                        detail = uiState.packageDetail!!,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }

            if (uiState.showAddToCartSuccess) {
                AddToCartSuccessDialog(onDismiss = { viewModel.dismissAddToCartDialog() })
            }
        }
    }
}

@Composable
private fun PackageDetailContent(
    detail: PackageDetail,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(2.dp, LightBlueBorder, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.Top) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(PrimaryBlue.copy(alpha = 0.1f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = packageIconFor(detail.iconType),
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = detail.serviceTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = detail.serviceDescription,
                            color = Color.DarkGray,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFEDF2F7), RoundedCornerShape(12.dp))
                        .padding(16.dp)
                ) {
                    Text(text = "Phí tiện ích", color = TextGray, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = detail.currentPrice,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = detail.discountPercent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = DiscountRed
                        )
                    }
                    Text(
                        text = detail.originalPrice,
                        color = TextGray,
                        fontSize = 12.sp,
                        textDecoration = TextDecoration.LineThrough
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                DetailRow(label = "Tên gói", value = detail.packageName)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFFE2E8F0)
                )
                DetailRow(label = "Thời hạn", value = detail.duration)
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 12.dp),
                    color = Color(0xFFE2E8F0)
                )
                DetailRow(label = "Lượt đăng ký", value = detail.registrationCountLabel)
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
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextGray, fontSize = 14.sp)
        Text(
            text = value,
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

private fun packageIconFor(iconType: String): ImageVector = when (iconType) {
    "pool" -> Icons.Default.Pool
    else -> Icons.Default.FitnessCenter
}

@Preview(showBackground = true, name = "Chi tiết gói dịch vụ cụ thể")
@Composable
fun PackageDetailScreenPreview() {
    BMISTheme {
        PackageDetailScreen(serviceId = "1", packageId = "1")
    }
}
