package com.example.bmis.ui.screens.ThongTinDangKy

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.data.models.EmployeeInfo
import com.example.bmis.data.models.RegistrationDetail
import com.example.bmis.data.models.RegistrationOrderStatus
import com.example.bmis.data.models.RegistrationTicketItem
import com.example.bmis.ui.theme.BMISTheme

private val PrimaryBlue = Color(0xFF0F3C88)
private val TextGray = Color(0xFF8A92A6)
private val BackgroundColor = Color(0xFFF4F6F9)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationDetailScreen(
    registrationId: String = "0",
    status: RegistrationOrderStatus = RegistrationOrderStatus.PENDING,
    onBackClick: () -> Unit = {},
    onCancelRegistrationClick: () -> Unit = {},
    viewModel: RegistrationDetailViewModel = viewModel(
        factory = RegistrationDetailViewModel.provideFactory(registrationId, status)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val detail = uiState.detail

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Thông tin đăng ký",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            detail?.let { registration ->
                if (registration.status == RegistrationOrderStatus.PENDING) {
                    PendingBottomBar(
                        totalAmount = registration.totalAmount,
                        onCancelRegistrationClick = onCancelRegistrationClick
                    )
                } else {
                    RegistrationBottomNavigationBar()
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
                detail != null -> {
                    RegistrationDetailContent(
                        detail = detail,
                        paddingValues = paddingValues,
                        onEmployeeClick = { viewModel.showEmployeeDialog() }
                    )
                }
            }

            val employee = detail?.employee
            if (uiState.showEmployeeDialog && employee != null) {
                EmployeeInfoDialog(
                    employee = employee,
                    onDismiss = { viewModel.dismissEmployeeDialog() }
                )
            }
        }
    }
}

@Composable
private fun RegistrationDetailContent(
    detail: RegistrationDetail,
    paddingValues: PaddingValues,
    onEmployeeClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(horizontal = 16.dp),
        contentPadding = PaddingValues(vertical = 20.dp)
    ) {
        item {
            Text(
                text = "Tiện ích đăng ký",
                color = PrimaryBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        items(detail.tickets, key = { it.id }) { item ->
            RegistrationTicketRow(item = item)
            HorizontalDivider(
                color = Color(0xFFF1F5F9),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Thông tin đăng ký",
                color = PrimaryBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Spacer(modifier = Modifier.height(12.dp))

            RegistrationInfoSection(detail = detail, onEmployeeClick = onEmployeeClick)

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Ghi chú", color = TextGray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = detail.note, color = Color.LightGray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun RegistrationTicketRow(item: RegistrationTicketItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = ticketIconFor(item.iconType),
                contentDescription = null,
                tint = ticketIconTintFor(item.iconType),
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
            Text(text = item.packageName, color = TextGray, fontSize = 12.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = item.price,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color.Black
            )
        }
        Text(
            text = "Số lượng: ${item.quantity}",
            color = Color.DarkGray,
            fontSize = 12.sp
        )
    }
}

@Composable
private fun RegistrationInfoSection(
    detail: RegistrationDetail,
    onEmployeeClick: () -> Unit
) {
    val status = detail.status

    InfoDetailRow(label = "Mã đăng ký", value = detail.registrationCode)

    if (status == RegistrationOrderStatus.CANCELLED) {
        InfoDetailRow(label = "Tổng tiền", value = detail.totalAmount)
    }

    InfoDetailRow(
        label = "Tình trạng",
        value = status.title,
        valueColor = statusColorFor(status)
    )
    InfoDetailRow(label = "Căn hộ", value = detail.apartment)
    InfoDetailRow(label = "Số điện thoại đăng ký", value = detail.phone)
    InfoDetailRow(label = "Ngày đăng ký", value = detail.registrationDate)

    if (status == RegistrationOrderStatus.PAID || status == RegistrationOrderStatus.CANCELLED) {
        InfoDetailRow(label = "Ngày thanh toán", value = detail.paymentDate)
    } else {
        InfoDetailRow(label = "Ngày thanh toán", value = "")
    }

    if (status == RegistrationOrderStatus.PAID) {
        InfoDetailRow(label = "Hình thức thanh toán", value = detail.paymentMethod)
    }

    if (status == RegistrationOrderStatus.CANCELLED) {
        InfoDetailRow(label = "Hình thức thanh toán", value = "")
        InfoDetailRow(label = "Ngày hủy", value = detail.cancellationDate)
    } else if (status == RegistrationOrderStatus.PENDING) {
        InfoDetailRow(label = "Ngày hủy", value = "")
    }

    if (status == RegistrationOrderStatus.PAID || status == RegistrationOrderStatus.CANCELLED) {
        InfoDetailRow(
            label = "Nhân viên xử lý trực tiếp",
            value = detail.employee?.name.orEmpty(),
            valueColor = PrimaryBlue,
            onClick = onEmployeeClick
        )
    } else {
        InfoDetailRow(label = "Nhân viên xử lý trực tiếp", value = "")
    }
}

@Composable
private fun PendingBottomBar(
    totalAmount: String,
    onCancelRegistrationClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tổng tiền",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = Color.Black
            )
            Text(
                text = totalAmount,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFFF4D4D)
            )
        }
        Button(
            onClick = onCancelRegistrationClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4D4D)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                "Hủy đăng ký",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun RegistrationBottomNavigationBar() {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Home, contentDescription = null) },
            label = { Text("Trang chủ", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Group, contentDescription = null) },
            label = { Text("Thành viên", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.CreditCard, contentDescription = null) },
            label = { Text("Pay", fontSize = 10.sp) }
        )
        NavigationBarItem(
            selected = false,
            onClick = { },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Cá nhân", fontSize = 10.sp) }
        )
    }
}

@Composable
private fun EmployeeInfoDialog(
    employee: EmployeeInfo,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                IconButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = TextGray)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Thông tin nhân viên\nxử lý",
                        color = PrimaryBlue,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center,
                        lineHeight = 24.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .background(Color(0xFFE6F0FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(60.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = employee.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = PrimaryBlue
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = employee.phone, fontSize = 14.sp, color = Color.DarkGray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = employee.email, fontSize = 14.sp, color = Color.DarkGray)
                }
            }
        }
    }
}

@Composable
private fun InfoDetailRow(
    label: String,
    value: String,
    valueColor: Color = Color.Black,
    onClick: (() -> Unit)? = null
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, color = TextGray, fontSize = 13.sp)
            Text(
                text = value.ifEmpty { "—" },
                color = if (value.isEmpty()) Color.LightGray else valueColor,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            )
        }
        HorizontalDivider(color = Color(0xFFF1F5F9))
    }
}

private fun ticketIconFor(iconType: String): ImageVector = when (iconType) {
    "pool" -> Icons.Default.Pool
    else -> Icons.Default.FitnessCenter
}

private fun ticketIconTintFor(iconType: String): Color = when (iconType) {
    "pool" -> Color(0xFF3EA3FF)
    else -> PrimaryBlue
}

private fun statusColorFor(status: RegistrationOrderStatus): Color = when (status) {
    RegistrationOrderStatus.PENDING -> Color(0xFFFFA800)
    RegistrationOrderStatus.PAID -> Color(0xFF27AE60)
    RegistrationOrderStatus.CANCELLED -> Color(0xFFFF4D4D)
}

@Preview(showBackground = true, name = "Trạng thái Đã thanh toán")
@Composable
fun PreviewPaidRegistration() {
    BMISTheme {
        RegistrationDetailScreen(
            registrationId = "0",
            status = RegistrationOrderStatus.PAID
        )
    }
}
