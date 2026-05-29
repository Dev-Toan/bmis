package com.example.bmis.ui.screens.GioHang

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

private val PrimaryBlue = Color(0xFF0F3C88)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)
private val OrangeButton = Color(0xFFFF912C)
private val GrayRowBg = Color(0xFFF8FAFC)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBackClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    viewModel: CartViewModel = viewModel(factory = CartViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Giỏ hàng (${uiState.items.size})", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextGray)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 16.dp)
            ) {
                Button(
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeButton),
                    shape = RoundedCornerShape(27.dp)
                ) {
                    Text("Đăng ký", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                else -> {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .padding(horizontal = 16.dp),
                contentPadding = PaddingValues(vertical = 20.dp)
            ) {
                item {
                    SectionHeader(title = "Chọn tiện ích đăng ký")
                    Spacer(modifier = Modifier.height(12.dp))
                }

                items(uiState.items, key = { it.id }) { item ->
                    val isChecked = uiState.checkedStates[item.id] ?: false
                    val quantity = uiState.quantities[item.id] ?: 1

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { viewModel.toggleChecked(item.id, it) },
                            colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                        )

                        Spacer(modifier = Modifier.width(4.dp))

                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = cartIconFor(item.iconType),
                                contentDescription = null,
                                tint = cartIconTintFor(item.iconType),
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = item.title, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.Black)
                            Text(text = item.packageName, color = TextGray, fontSize = 12.sp)
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = item.price, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black)
                        }

                        Row(
                            modifier = Modifier
                                .background(GrayRowBg, RoundedCornerShape(6.dp))
                                .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(6.dp)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(
                                onClick = { viewModel.updateQuantity(item.id, -1) },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Giảm", modifier = Modifier.size(16.dp), tint = TextGray)
                            }
                            Text(
                                text = quantity.toString(),
                                modifier = Modifier.padding(horizontal = 12.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            IconButton(
                                onClick = { viewModel.updateQuantity(item.id, 1) },
                                modifier = Modifier.size(30.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Tăng", modifier = Modifier.size(16.dp), tint = PrimaryBlue)
                            }
                        }
                    }
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 8.dp))
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    SectionHeader(title = "Thông tin đăng ký")
                    Spacer(modifier = Modifier.height(12.dp))

                    InfoRowStatic(registrantName = uiState.registrantName)
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 12.dp))

                    InfoRowClickable(
                        label = "Căn hộ",
                        value = uiState.confirmedApartment,
                        onClick = { viewModel.openApartmentSheet() }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 12.dp))

                    InfoRowClickable(
                        label = "Số điện thoại đăng ký",
                        value = uiState.confirmedPhone,
                        onClick = { viewModel.openPhoneSheet() }
                    )
                    HorizontalDivider(color = Color(0xFFF1F5F9), modifier = Modifier.padding(vertical = 12.dp))

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(text = "Ghi chú", color = PrimaryBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = uiState.noteText,
                        onValueChange = { viewModel.updateNote(it) },
                        placeholder = { Text("Nhập ghi chú", color = TextGray, fontSize = 14.sp) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(90.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color(0xFFE2E8F0),
                            focusedBorderColor = PrimaryBlue,
                            focusedContainerColor = Color.Transparent,  // Hoặc Color.White tùy bạn muốn
                            unfocusedContainerColor = Color.Transparent
                        )
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(24.dp))
                    SectionHeader(title = "Hình thức thanh toán")
                    Spacer(modifier = Modifier.height(12.dp))

                    uiState.paymentMethods.forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updatePaymentMethod(method) }
                                .padding(vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (uiState.selectedPaymentMethod == method),
                                onClick = { viewModel.updatePaymentMethod(method) },
                                colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = method,
                                fontSize = 14.sp,
                                fontWeight = if (uiState.selectedPaymentMethod == method) FontWeight.Bold else FontWeight.Normal,
                                color = if (uiState.selectedPaymentMethod == method) PrimaryBlue else Color.Black
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
                }
            }

            if (uiState.showApartmentSheet) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.dismissApartmentSheet() },
                    dragHandle = { BottomSheetDragHandle() }
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(bottom = 32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "Chọn căn hộ", color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(bottom = 16.dp))
                        uiState.apartmentOptions.forEach { apartment ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.selectTemporaryApartment(apartment) }
                                    .padding(vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = (uiState.temporaryApartment == apartment),
                                    onClick = { viewModel.selectTemporaryApartment(apartment) },
                                    colors = RadioButtonDefaults.colors(selectedColor = PrimaryBlue)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = apartment,
                                    fontSize = 14.sp,
                                    color = if (uiState.temporaryApartment == apartment) PrimaryBlue else Color.Black,
                                    fontWeight = if (uiState.temporaryApartment == apartment) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                            HorizontalDivider(color = Color(0xFFF1F5F9))
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.confirmApartment() },
                            modifier = Modifier.fillMaxWidth().height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(text = "Chọn", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (uiState.showPhoneSheet) {
                ModalBottomSheet(
                    onDismissRequest = { viewModel.dismissPhoneSheet() },
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
                            text = "Số điện thoại đăng ký",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            modifier = Modifier.padding(bottom = 20.dp)
                        )

                        OutlinedTextField(
                            value = uiState.temporaryPhone,
                            onValueChange = { viewModel.updateTemporaryPhone(it) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            textStyle = LocalTextStyle.current.copy(
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = Color.Black
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color(0xFFE2E8F0),
                                focusedBorderColor = PrimaryBlue,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )

                        Spacer(modifier = Modifier.height(30.dp))

                        Button(
                            onClick = { viewModel.confirmPhone() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                            shape = RoundedCornerShape(24.dp)
                        ) {
                            Text(text = "Hoàn thành", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

private fun cartIconFor(iconType: String): ImageVector = when (iconType) {
    "pool" -> Icons.Default.Pool
    else -> Icons.Default.FitnessCenter
}

private fun cartIconTintFor(iconType: String): Color = when (iconType) {
    "pool" -> Color(0xFF3EA3FF)
    else -> PrimaryBlue
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
private fun SectionHeader(title: String) {
    Text(text = title, color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 16.sp)
}

@Composable
private fun InfoRowStatic(registrantName: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Người đăng ký", color = TextGray, fontSize = 14.sp)
        Text(text = registrantName, color = PrimaryBlue, fontWeight = FontWeight.Bold, fontSize = 14.sp)
    }
}

@Composable
private fun InfoRowClickable(label: String, value: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextGray, fontSize = 14.sp)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = value, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextGray, modifier = Modifier.size(20.dp))
        }
    }
}