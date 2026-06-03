package com.example.bmis.ui.screens.LuongSuaChua.ThongTinChiTietDangKy

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.R
import com.example.bmis.data.models.RepairStatus
import com.example.bmis.data.repository.FakeRepairRepository
import com.example.bmis.ui.theme.BMISTheme

private val PrimaryBlue = Color(0xFF0F3C88)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)
private val StatusRed = Color(0xFFF44336)
private val StatusOrange = Color(0xFFFF9800)
private val StatusGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThongTinChiTietScreen(
    registrationId: String = "DK0001",
    status: RepairStatus = RepairStatus.PROCESSED,
    onBackClick: () -> Unit = {},
    viewModel: ThongTinChiTietViewModel = viewModel(
        factory = ThongTinChiTietViewModel.provideFactory(
            repository = FakeRepairRepository(),
            registrationId = registrationId,
            status = status
        )
    )
) {
    val uiState by viewModel.uiState.collectAsState()

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
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextGray
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .background(Color.White)
            ) {
                // Header: Tên dịch vụ
                Text(
                    text = uiState.serviceName,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(16.dp)
                )

                // Thông tin cơ bản
                DetailRow(label = "Mã đăng ký", value = uiState.registrationId, isBoldValue = true)
                DetailRow(label = "Căn hộ", value = uiState.apartment, isBoldValue = true)

                // Tình trạng
                StatusDetailRow(status = uiState.status)

                DetailRow(label = "Mô tả", value = uiState.description, isBoldValue = true)

                // Ảnh mô tả
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Ảnh mô tả",
                        fontSize = 14.sp,
                        color = TextGray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.chuachay),
                        contentDescription = "Repair Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }

                // Timeline/Process Info
                DetailRow(label = "Ngày đăng ký", value = uiState.registrationDate, isBoldValue = true)

                if (uiState.status != RepairStatus.PENDING) {
                    DetailRow(
                        label = "Nhân viên tiếp nhận",
                        value = uiState.receptionStaff,
                        valueColor = PrimaryBlue,
                        isBoldValue = true
                    )
                    DetailRow(
                        label = "Ngày tiếp nhận",
                        value = uiState.receptionDate,
                        isBoldValue = true
                    )
                    DetailRow(
                        label = "Nhân viên sửa chữa",
                        value = uiState.repairStaff,
                        valueColor = PrimaryBlue,
                        isBoldValue = true
                    )
                }

                if (uiState.status == RepairStatus.PROCESSED) {
                    DetailRow(
                        label = "Ngày xử lý",
                        value = uiState.processedDate,
                        isBoldValue = true
                    )
                    DetailRow(
                        label = "Kết quả xử lý",
                        value = uiState.repairResult,
                        isBoldValue = true
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = Color.Black,
    isBoldValue: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = TextGray,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                fontSize = 14.sp,
                color = valueColor,
                fontWeight = if (isBoldValue) FontWeight.Bold else FontWeight.Normal,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.End
            )
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 0.5.dp,
            color = Color.LightGray.copy(alpha = 0.5f)
        )
    }
}

@Composable
private fun StatusDetailRow(status: RepairStatus) {
    val (text, color, icon) = when (status) {
        RepairStatus.PENDING -> Triple("Chưa tiếp nhận", StatusRed, Icons.Default.Error)
        RepairStatus.RECEIVED -> Triple("Đã tiếp nhận", StatusOrange, Icons.Default.Refresh)
        RepairStatus.PROCESSED -> Triple("Đã xử lý", StatusGreen, Icons.Default.CheckCircle)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tình trạng",
                fontSize = 14.sp,
                color = TextGray
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = color,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        HorizontalDivider(
            modifier = Modifier.padding(top = 8.dp),
            thickness = 0.5.dp,
            color = Color.LightGray.copy(alpha = 0.5f)
        )
    }
}

@Preview(showBackground = true, name = "Chưa tiếp nhận")
@Composable
fun PreviewPending() {
    BMISTheme {
        ThongTinChiTietScreen(status = RepairStatus.PENDING)
    }
}

@Preview(showBackground = true, name = "Đã tiếp nhận")
@Composable
fun PreviewReceived() {
    BMISTheme {
        ThongTinChiTietScreen(status = RepairStatus.RECEIVED)
    }
}

@Preview(showBackground = true, name = "Đã xử lý")
@Composable
fun PreviewProcessed() {
    BMISTheme {
        ThongTinChiTietScreen(status = RepairStatus.PROCESSED)
    }
}
