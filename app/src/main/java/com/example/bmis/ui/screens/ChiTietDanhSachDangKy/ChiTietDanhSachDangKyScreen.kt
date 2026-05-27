package com.example.bmis.ui.screens.services

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.ui.screens.ChiTietDanhSachDangKy.ItemsServicesViewModel
import com.example.bmis.ui.theme.BMISTheme
import com.example.bmis.ui.theme.*
import androidx.compose.runtime.getValue
import com.example.bmis.data.models.ItemsServices

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaidServicesScreen(
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onSortClick: () -> Unit = {},
    onFilterClick: () -> Unit = {},
    statusType: StatusType = StatusType.SUCCESS,
    viewModel: ItemsServicesViewModel = viewModel(),
) {
    val statusColor = when (statusType) {
        StatusType.SUCCESS -> Color(0xFF4CAF50) // Xanh lá
        StatusType.WARNING -> Color(0xFFFF9800) // Cam
        StatusType.ERROR -> Color(0xFFF44336)   // Đỏ
    }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Đã thanh toán",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextGray
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onCartClick) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Cart",
                            tint = TextGray
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Thanh công cụ: Tất cả & Lọc (1)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Nút Sắp xếp / Tất cả bên trái
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onSortClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapVert,
                        contentDescription = "Sort",
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tất cả",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Nút Bộ lọc (1) bên phải
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onFilterClick() }
                ) {
                    Icon(
                        imageVector = Icons.Default.FilterList,
                        contentDescription = "Filter",
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Lọc (1)",
                        fontSize = 14.sp,
                        color = Color.Black,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Danh sách nội dung hóa đơn (Nền trắng bo nhẹ góc phía trên như hình)
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                color = Color.White
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    itemsIndexed(uiState.invoices) { index, invoice ->
                        InvoiceItemRow(invoice = invoice, color = statusColor)

                        // Thêm đường kẻ phân cách giữa các phần tử (Trừ phần tử cuối cùng)
                        if (index < uiState.invoices.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                color = Color(0xFFF0F0F0),
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InvoiceItemRow(invoice: ItemsServices, color: Color, ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Mã đăng ký (Màu xanh lá làm điểm nhấn trạng thái)
        Text(
            text = "Mã đăng ký: ${invoice.registrationCode}",
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Tên các dịch vụ tiện ích đã chọn
        Text(
            text = invoice.servicesName,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Dòng thông tin: Ngày đăng ký
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Ngày đăng ký: ", color = Color.Gray, fontSize = 13.sp)
            Text(text = invoice.registrationDate, color = Color.DarkGray, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Dòng thông tin: Ngày thanh toán
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Ngày thanh toán: ", color = Color.Gray, fontSize = 13.sp)
            Text(text = invoice.paymentDate, color = Color.DarkGray, fontSize = 13.sp)
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Dòng thông tin: Tổng tiền
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Tổng tiền: ", color = Color.Gray, fontSize = 13.sp)
            Text(text = invoice.totalAmount, color = Color.DarkGray, fontSize = 13.sp)
        }
    }
}

@Preview(showBackground = true, name = "Màn hình Đã thanh toán")
@Composable
fun PaidServicesScreenPreview() {
    BMISTheme {
        PaidServicesScreen()
    }
}