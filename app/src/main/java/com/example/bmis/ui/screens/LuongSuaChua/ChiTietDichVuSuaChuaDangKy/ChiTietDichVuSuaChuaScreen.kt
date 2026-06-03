package com.example.bmis.ui.screens.LuongSuaChua.ChiTietDichVuSuaChuaDangKy


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.data.models.FilterStep
import com.example.bmis.data.models.ItemsServices
import com.example.bmis.data.models.SortOption
import com.example.bmis.ui.screens.services.StatusType
import com.example.bmis.ui.theme.BMISTheme

private val PrimaryBlue = Color(0xFF0F3C88)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)
private val YearOrange = Color(0xFFFF8A00)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ChiTietDichVuSuaChuaScreen(
    onBackClick: () -> Unit = {},
    onCartClick: () -> Unit = {},
    onItemClick: (ItemsServices, StatusType) -> Unit = { _, _ -> },
    statusType: StatusType = StatusType.SUCCESS,
    viewModel: PaidServicesViewModel = viewModel(
        factory = PaidServicesViewModel.provideFactory(statusType)
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    val statusColor = statusColorFor(statusType)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = uiState.screenTitle.ifEmpty { titleFor(statusType) },
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
                    ChiTietDichVuSuaChuaContent(
                        uiState = uiState,
                        statusColor = statusColor,
                        statusType = statusType,
                        paddingValues = paddingValues,
                        onItemClick = onItemClick,
                        viewModel = viewModel
                    )
                }
            }

            if (uiState.showSortSheet) {
                SortBottomSheet(
                    temporarySortOption = uiState.temporarySortOption,
                    onSelectSort = viewModel::selectTemporarySort,
                    onDismiss = viewModel::dismissSortSheet,
                    onApply = viewModel::applySort
                )
            }

            if (uiState.showFilterSheet) {
                FilterBottomSheet(
                    uiState = uiState,
                    viewModel = viewModel
                )
            }
        }
    }
}

@Composable
private fun ChiTietDichVuSuaChuaContent(
    uiState: PaidServicesUiState,
    statusColor: Color,
    statusType: StatusType,
    paddingValues: PaddingValues,
    onItemClick: (ItemsServices, StatusType) -> Unit,
    viewModel: PaidServicesViewModel
) {
    Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { viewModel.openSortSheet() }
            ) {
                Icon(
                    Icons.Default.SwapVert,
                    contentDescription = "Sort",
                    tint = TextGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    uiState.appliedSortOption.title,
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.clickable { viewModel.openFilterSheet() }
            ) {
                Icon(
                    Icons.Default.FilterList,
                    contentDescription = "Filter",
                    tint = TextGray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    "Lọc (${uiState.activeFilterCount})",
                    fontSize = 14.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = Color.White
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(
                    items = uiState.displayedInvoices,
                    key = { _, invoice -> invoice.id }
                ) { index, invoice ->
                    InvoiceItemRow(
                        invoice = invoice,
                        color = statusColor,
                        statusType = statusType,
                        onClick = { onItemClick(invoice, statusType) }
                    )
                    if (index < uiState.displayedInvoices.lastIndex) {
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SortBottomSheet(
    temporarySortOption: SortOption,
    onSelectSort: (SortOption) -> Unit,
    onDismiss: () -> Unit,
    onApply: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        dragHandle = { BottomSheetDragHandle() }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                "Sắp xếp",
                color = PrimaryBlue,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            SortOption.entries.forEach { option ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelectSort(option) }
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (temporarySortOption == option),
                        onClick = { onSelectSort(option) },
                        colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        option.title,
                        fontSize = 15.sp,
                        color = Color.Black,
                        fontWeight = if (temporarySortOption == option) FontWeight.Bold else FontWeight.Normal
                    )
                }
                HorizontalDivider(color = Color(0xFFF1F5F9))
            }
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onApply,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Áp dụng", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun FilterBottomSheet(
    uiState: PaidServicesUiState,
    viewModel: PaidServicesViewModel
) {
    ModalBottomSheet(
        onDismissRequest = viewModel::dismissFilterSheet,
        containerColor = Color.White,
        dragHandle = { BottomSheetDragHandle() }
    ) {
        when (uiState.currentFilterStep) {
            FilterStep.MAIN -> FilterMainStep(uiState, viewModel)
            FilterStep.TIME -> FilterTimeStep(uiState, viewModel)
            FilterStep.SERVICES -> FilterServicesStep(uiState, viewModel)
            FilterStep.APARTMENT -> FilterApartmentStep(uiState, viewModel)
        }
    }
}

@Composable
private fun FilterMainStep(uiState: PaidServicesUiState, viewModel: PaidServicesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            "Bộ lọc",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        FilterOptionRow(
            label = "Thời gian",
            value = uiState.appliedTime ?: "Tất cả",
            onClick = { viewModel.openFilterStep(FilterStep.TIME) }
        )
        HorizontalDivider(color = Color(0xFFF1F5F9))

        val serviceText = if (uiState.appliedServices.isEmpty()) {
            "Tất cả"
        } else {
            uiState.appliedServices.joinToString(", ")
        }
        FilterOptionRow(
            label = "Dịch vụ tiện ích (${uiState.appliedServices.size})",
            value = serviceText,
            onClick = { viewModel.openFilterStep(FilterStep.SERVICES) }
        )
        HorizontalDivider(color = Color(0xFFF1F5F9))

        FilterOptionRow(
            label = "Căn hộ",
            value = uiState.appliedApartment ?: "Tất cả",
            onClick = { viewModel.openFilterStep(FilterStep.APARTMENT) }
        )
        HorizontalDivider(color = Color(0xFFF1F5F9))

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.resetFilters() },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(1.dp, PrimaryBlue)
            ) {
                Text("Làm mới", color = PrimaryBlue, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.applyFilterSheet() },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Áp dụng", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun FilterTimeStep(uiState: PaidServicesUiState, viewModel: PaidServicesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            "Thời gian",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
            items(2) { yearOffset ->
                val year = 2019 + yearOffset
                Text(
                    text = year.toString(),
                    color = YearOrange,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    maxItemsInEachRow = 4
                ) {
                    for (month in 1..12) {
                        val monthStr = "Tháng $month - $year"
                        val isSelected = uiState.tempTime == monthStr
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .border(
                                    1.dp,
                                    if (isSelected) PrimaryBlue else Color(0xFFE2E8F0),
                                    RoundedCornerShape(8.dp)
                                )
                                .background(
                                    if (isSelected) PrimaryBlue.copy(alpha = 0.1f) else Color.White,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable { viewModel.selectTempTime(monthStr) }
                                .padding(vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    "Tháng",
                                    fontSize = 12.sp,
                                    color = if (isSelected) PrimaryBlue else TextGray
                                )
                                Text(
                                    String.format("%02d", month),
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) PrimaryBlue else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.clearTimeFilter() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Bỏ lọc thời gian", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FilterServicesStep(uiState: PaidServicesUiState, viewModel: PaidServicesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            "Bộ lọc",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        uiState.serviceFilterOptions.forEach { service ->
            val isChecked = uiState.tempServices.contains(service)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { viewModel.toggleTempService(service) }
                    .padding(vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = null,
                    colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(service, fontSize = 15.sp, color = Color.Black)
            }
            HorizontalDivider(color = Color(0xFFF1F5F9))
        }

        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = { viewModel.clearTempServices() },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                shape = RoundedCornerShape(25.dp),
                border = BorderStroke(1.dp, PrimaryBlue)
            ) {
                Text("Bỏ chọn", color = PrimaryBlue, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = { viewModel.confirmTempServices() },
                modifier = Modifier
                    .weight(1f)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(25.dp)
            ) {
                Text("Chọn", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun FilterApartmentStep(uiState: PaidServicesUiState, viewModel: PaidServicesViewModel) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)
    ) {
        Text(
            "Bộ lọc",
            color = PrimaryBlue,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            textAlign = TextAlign.Center
        )

        LazyColumn(modifier = Modifier.weight(1f, fill = false)) {
            items(uiState.apartmentFilterOptions, key = { it }) { apartment ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { viewModel.selectTempApartment(apartment) }
                        .padding(vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = (uiState.tempApartment == apartment),
                        onClick = { viewModel.selectTempApartment(apartment) },
                        colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(apartment, fontSize = 15.sp, color = Color.Black)
                }
                HorizontalDivider(color = Color(0xFFF1F5F9))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { viewModel.confirmTempApartment() },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
            shape = RoundedCornerShape(25.dp)
        ) {
            Text("Chọn", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FilterOptionRow(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(label, color = Color.Black, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = PrimaryBlue, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        }
        Icon(
            Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = TextGray,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun BottomSheetDragHandle() {
    Box(
        modifier = Modifier
            .padding(vertical = 12.dp)
            .size(width = 40.dp, height = 4.dp)
            .background(Color(0xFFE2E8F0), RoundedCornerShape(2.dp))
    )
}

@Composable
private fun InvoiceItemRow(invoice: ItemsServices, color: Color,statusType: StatusType, onClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Text(
            "Mã đăng ký: ${invoice.registrationCode}",
            color = color,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
        Spacer(modifier = Modifier.height(4.dp))

        InfoRow(label = "Căn hộ:", value = invoice.apartment)

        InfoRow(label = "Ngày thanh toán:", value = invoice.paymentDate.ifEmpty { "21/09/2019" })

        when (statusType) {
            StatusType.WARNING -> { // Tương ứng "Đã tiếp nhận"
                InfoRow(label = "Nhân viên sửa chữa:", value = "Nguyễn Văn A")
            }
            StatusType.SUCCESS -> { // Tương ứng "Đã xử lý"
                InfoRow(label = "Kết quả xử lý:", value = "Đã sửa")
            }
            StatusType.ERROR -> { }
        }
    }
}


@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.Gray, fontSize = 13.sp, modifier = Modifier.width(120.dp))
        Text(text = value, color = Color.DarkGray, fontSize = 13.sp, fontWeight = FontWeight.Medium)
    }
}

private fun statusColorFor(statusType: StatusType): Color = when (statusType) {
    StatusType.SUCCESS -> Color(0xFF4CAF50)
    StatusType.WARNING -> Color(0xFFFF9800)
    StatusType.ERROR -> Color(0xFFF44336)
}

private fun titleFor(statusType: StatusType): String = when (statusType) {
    StatusType.SUCCESS -> "Đã thanh toán"
    StatusType.WARNING -> "Chưa thanh toán"
    StatusType.ERROR -> "Đã hủy"
}

@Preview(showBackground = true, name = "Đã thanh toán (Bộ lọc)")
@Composable
fun PaidServicesScreenPreview() {
    BMISTheme {
        ChiTietDichVuSuaChuaScreen(statusType = StatusType.WARNING)
    }
}
