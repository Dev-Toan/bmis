package com.example.bmis.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.R
import com.example.bmis.ui.theme.BMISTheme

private val PrimaryBlue = Color(0xFF0F3C88)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)
private val OrangeHighlight = Color(0xFFFF9800)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    onLogout: () -> Unit = {},
    viewModel: ProfileViewModel = viewModel() // Cần chắc chắn bạn đã có ViewModel này
) {
    val uiState by viewModel.uiState.collectAsState()

    // Định nghĩa vài màu sắc cơ bản từ ảnh (bạn có thể thay thế bằng Theme colors của project)
    val TextDarkBlue = Color(0xFF141433)
    val TextGray = Color(0xFF7A828A)
    val OrangeHighlight = Color(0xFFF2994A)
    val BorderOrange = Color(0xFFF2994A) // Màu viền avatar
    val BorderBlue = Color(0xFF2D5C8F) // Màu viền avatar (nếu bạn muốn làm gradient thì cần dùng Brush)

    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color(0xFFF5F6F8) // BackgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                // --- HEADER SECTION ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // Tổng chiều cao phần header
                ) {
                    // 1. Ảnh bìa (Banner)
                    Image(
                        painter = painterResource(id = R.drawable.banner), // Thay bằng drawable của bạn
                        contentDescription = "Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(210.dp), // Chiều cao của banner ngắn hơn tổng Box
                        contentScale = ContentScale.Crop
                    )

                    // 2. Avatar và Thông tin (Nằm đè lên viền dưới của Banner)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomStart)
                            .padding(start = 16.dp, end = 5.dp), // Căn lề trái và dưới
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Surface(
                            modifier = Modifier
                                .size(110.dp)
                                .border(
                                    width = 4.dp,
                                    color = BorderOrange, // Tạm dùng viền cam, có thể đổi sang Brush gradient nếu muốn
                                    shape = CircleShape
                                )
                                .padding(4.dp) // Khoảng cách giữa viền và ảnh
                                .clip(CircleShape),
                            color = Color.White
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.chuachay), // Thay bằng ảnh avatar của bạn
                                contentDescription = "Avatar",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Tên và số điện thoại
                        Column(
                            modifier = Modifier.padding(top = 30.dp) // Đẩy text xuống một chút để cân đối with avatar
                        ) {
                            Text(
                                text = uiState.userName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                color = TextDarkBlue
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Outlined.PhoneAndroid, // Icon điện thoại dạng viền
                                    contentDescription = null,
                                    tint = TextGray,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = uiState.phone,
                                    color = TextGray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(60.dp))

                // Menu List
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column {
                        ProfileMenuItem(
                            icon = Icons.Default.Business,
                            title = "Danh sách căn hộ",
                            onClick = { viewModel.toggleApartmentSheet(true) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = BackgroundColor)
                        ProfileMenuItem(
                            icon = Icons.Default.Security,
                            title = "Thiết lập bảo mật",
                            onClick = { viewModel.toggleSecuritySheet(true) }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = BackgroundColor)
                        ProfileMenuItem(
                            icon = Icons.Default.Share,
                            title = "Chia sẻ ứng dụng",
                            onClick = { /* Handle share */ }
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = BackgroundColor)
                        ProfileMenuItem(
                            icon = Icons.Default.Domain,
                            title = "Đổi dự án",
                            onClick = { /* Handle change project */ }
                        )
                    }
                }

                // Khoảng trống dưới cùng để không bị nút Đăng xuất che nội dung menu cuối khi cuộn hết cỡ
                Spacer(modifier = Modifier.height(100.dp))
            }

            // Logout Button - Cố định ở dưới cùng
            TextButton(
                onClick = onLogout,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, tint = OrangeHighlight)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Đăng xuất", color = OrangeHighlight, fontWeight = FontWeight.Medium)
                }
            }
        }

        // Dialogs & Sheets
        if (uiState.showApartmentSheet) {
            ApartmentBottomSheet(
                apartments = uiState.apartments,
                onDismiss = { viewModel.toggleApartmentSheet(false) }
            )
        }

        if (uiState.showSecuritySheet) {
            SecurityBottomSheet(
                isFingerprintEnabled = uiState.isFingerprintEnabled,
                onFingerprintToggle = viewModel::setFingerprintEnabled,
                onChangePasswordClick = { viewModel.toggleChangePasswordDialog(true) },
                onDismiss = { viewModel.toggleSecuritySheet(false) }
            )
        }

        if (uiState.showChangePasswordDialog) {
            ChangePasswordDialog(
                onDismiss = { viewModel.toggleChangePasswordDialog(false) },
                onConfirm = { viewModel.toggleSuccessPopup(true) }
            )
        }

        if (uiState.showSuccessPopup) {
            SuccessPopup(
                message = "Đổi mật khẩu thành công",
                onDismiss = { viewModel.toggleSuccessPopup(false) }
            )
        }

        if (uiState.showAvatarOptions) {
            AvatarOptionsSheet(
                onDismiss = { viewModel.toggleAvatarOptions(false) }
            )
        }
    }
}

@Composable
private fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, modifier = Modifier.weight(1f), fontSize = 15.sp, color = Color.Black)
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextGray)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ApartmentBottomSheet(apartments: List<String>, onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color.White) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)) {
            Text(
                "Danh sách căn hộ",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            apartments.forEach { apartment ->
                Text(
                    text = apartment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    fontSize = 15.sp
                )
                HorizontalDivider(color = BackgroundColor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecurityBottomSheet(
    isFingerprintEnabled: Boolean,
    onFingerprintToggle: (Boolean) -> Unit,
    onChangePasswordClick: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color.White) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .padding(bottom = 32.dp)) {
            Text(
                "Thiết lập bảo mật",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Đăng nhập vân tay", fontSize = 15.sp)
                Switch(
                    checked = isFingerprintEnabled,
                    onCheckedChange = onFingerprintToggle,
                    colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = OrangeHighlight)
                )
            }
            HorizontalDivider(color = BackgroundColor)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onChangePasswordClick() }
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Đặt lại mật khẩu", fontSize = 15.sp)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextGray)
            }
        }
    }
}

@Composable
private fun ChangePasswordDialog(onDismiss: () -> Unit, onConfirm: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Đổi mật khẩu", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(24.dp))
                
                PasswordField(label = "Mật khẩu cũ", placeholder = "Nhập mật khẩu cũ")
                Spacer(modifier = Modifier.height(16.dp))
                PasswordField(label = "Mật khẩu mới", placeholder = "Nhập mật khẩu mới")
                Spacer(modifier = Modifier.height(16.dp))
                PasswordField(label = "Nhập lại mật khẩu", placeholder = "Nhập lại mật khẩu")
                
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onConfirm,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeHighlight),
                    shape = RoundedCornerShape(25.dp)
                ) {
                    Text("Xác nhận", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun PasswordField(label: String, placeholder: String) {
    Column {
        Text(label, fontSize = 14.sp, color = TextGray)
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text(placeholder, fontSize = 14.sp) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray
            )
        )
    }
}

@Composable
private fun SuccessPopup(message: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.width(200.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = OrangeHighlight, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(message, textAlign = TextAlign.Center, fontSize = 14.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AvatarOptionsSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(onDismissRequest = onDismiss, containerColor = Color.White) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp)) {
            Text(
                "Chọn avatar",
                modifier = Modifier.padding(16.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            AvatarOptionItem(icon = Icons.Default.AccountCircle, title = "Xem ảnh đại diện", onClick = onDismiss)
            AvatarOptionItem(icon = Icons.Default.CameraAlt, title = "Chụp ảnh", onClick = onDismiss)
            AvatarOptionItem(icon = Icons.Default.Image, title = "Chọn trong thư viện", onClick = onDismiss)
        }
    }
}

@Composable
private fun AvatarOptionItem(icon: ImageVector, title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryBlue)
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 15.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    BMISTheme {
        ProfileScreen()
    }
}
