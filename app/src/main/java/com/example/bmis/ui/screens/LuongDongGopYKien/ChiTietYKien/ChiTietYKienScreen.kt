package com.example.bmis.ui.screens.LuongDongGopYKien.ChiTietYKien

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.R
import com.example.bmis.data.models.Comment
import com.example.bmis.data.models.FeedbackStatus
import com.example.bmis.ui.theme.BMISTheme

private val PrimaryBlue = Color(0xFF0F3C88)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)
private val OrangeHighlight = Color(0xFFFF9800)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChiTietYKienScreen(
    onBackClick: () -> Unit = {},
    viewModel: ChiTietYKienViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Chi tiết ý kiến",
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            CommentInputBar(
                text = uiState.messageText,
                onTextChange = viewModel::onMessageChange,
                onSend = viewModel::sendMessage,
                onAttachClick = { viewModel.toggleImagePicker(true) }
            )
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            uiState.feedback?.let { feedback ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize().background(Color.White)
                ) {
                    item {
                        FeedbackHeaderInfo(feedback)
                        HorizontalDivider(thickness = 8.dp, color = BackgroundColor)
                    }

                    items(feedback.comments) { comment ->
                        CommentItem(comment)
                    }

                    if (feedback.status != FeedbackStatus.RESOLVED) {
                        item {
                            Box(modifier = Modifier.padding(16.dp)) {
                                Button(
                                    onClick = { /* Xử lý hoàn thành */ },
                                    modifier = Modifier.fillMaxWidth().height(50.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = OrangeHighlight),
                                    shape = RoundedCornerShape(25.dp)
                                ) {
                                    Text("Xác nhận hoàn thành", color = Color.White, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(100.dp))
                    }
                }
            }

            if (uiState.showStaffInfo) {
                StaffInfoDialog(onDismiss = { viewModel.toggleStaffInfo(false) })
            }

            if (uiState.showImagePicker) {
                ImagePickerBottomSheet(
                    onDismiss = { viewModel.toggleImagePicker(false) },
                    onSelectAll = { /* Logic */ }
                )
            }
        }
    }
}

@Composable
private fun FeedbackHeaderInfo(feedback: com.example.bmis.data.models.FeedbackDetail) {
    Column(modifier = Modifier.padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = feedback.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(
                text = feedback.status.title,
                color = feedback.status.color,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        InfoRow(label = "Căn hộ", value = feedback.apartment)
        InfoRow(label = "Loại", value = feedback.type)
        InfoRow(label = "Nội dung", value = feedback.content)
        
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Ảnh mô tả", fontSize = 14.sp, color = TextGray)
            Text(
                text = "Xem tất cả",
                color = PrimaryBlue,
                fontSize = 12.sp,
                modifier = Modifier.clickable { /* Mở gallery */ }
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(2) {
                Image(
                    painter = painterResource(id = R.drawable.chuachay),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp).clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(modifier = Modifier.padding(vertical = 2.dp)) {
        Text(text = "$label: ", color = TextGray, fontSize = 14.sp, modifier = Modifier.width(80.dp))
        Text(text = value, color = Color.Black, fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun CommentItem(comment: Comment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = if (comment.isStaff) Arrangement.Start else Arrangement.End
    ) {
        if (comment.isStaff) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(BackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Column(horizontalAlignment = if (comment.isStaff) Alignment.Start else Alignment.End) {
            Text(
                text = comment.userName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (comment.isStaff) PrimaryBlue else Color.Black
            )
            Text(text = comment.timestamp, fontSize = 10.sp, color = TextGray)
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                color = if (comment.isStaff) Color(0xFFF0F4FA) else Color(0xFFE8F5E9),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = comment.content,
                    modifier = Modifier.padding(12.dp),
                    fontSize = 14.sp
                )
            }
            if (comment.attachedImages.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    comment.attachedImages.forEach { _ ->
                        Image(
                            painter = painterResource(id = R.drawable.chuachay),
                            contentDescription = null,
                            modifier = Modifier.size(60.dp).clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CommentInputBar(
    text: String,
    onTextChange: (String) -> Unit,
    onSend: () -> Unit,
    onAttachClick: () -> Unit
) {
    Surface(
        color = Color.White,
        tonalElevation = 4.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onAttachClick) {
                Icon(Icons.Default.AttachFile, contentDescription = "Attach", tint = TextGray)
            }
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = { Text("Phản hồi tới: Lê Thanh Cường", fontSize = 14.sp) },
                modifier = Modifier.weight(1f),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
            )
            IconButton(onClick = onSend) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = PrimaryBlue)
            }
        }
    }
}

@Composable
private fun StaffInfoDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = onDismiss, modifier = Modifier.size(24.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextGray)
                    }
                }
                Text("Thông tin nhân viên xử lý", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.size(60.dp).clip(CircleShape).background(BackgroundColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(40.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text("Nguyễn Văn A", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("0912684123", color = TextGray)
                Text("nguyenvana@gmail.com", color = TextGray)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ImagePickerBottomSheet(onDismiss: () -> Unit, onSelectAll: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Tất cả ảnh", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                IconButton(onClick = { /* Gửi ảnh đã chọn */ }) {
                    Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = PrimaryBlue)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            // Grid ảnh (giả lập)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                repeat(3) {
                    Image(
                        painter = painterResource(id = R.drawable.chuachay),
                        contentDescription = null,
                        modifier = Modifier.size(100.dp).clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth().clickable { /* Chụp ảnh */ }.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CameraAlt, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Chụp ảnh")
            }
            Row(
                modifier = Modifier.fillMaxWidth().clickable { /* Thư viện */ }.padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Image, contentDescription = null, tint = PrimaryBlue)
                Spacer(modifier = Modifier.width(12.dp))
                Text("Chọn trong thư viện")
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChiTietYKienScreenPreview() {
    BMISTheme {
        ChiTietYKienScreen()
    }
}
