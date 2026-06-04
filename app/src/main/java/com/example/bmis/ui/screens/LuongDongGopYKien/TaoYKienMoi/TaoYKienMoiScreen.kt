package com.example.bmis.ui.screens.LuongDongGopYKien.TaoYKienMoi

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.bmis.ui.theme.BMISTheme

private val PrimaryBlue = Color(0xFF0F3C88)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)
private val OrangeHighlight = Color(0xFFFF9800)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaoYKienMoiScreen(
    onBackClick: () -> Unit = {},
    onSuccess: () -> Unit = {},
    viewModel: TaoYKienMoiViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            onSuccess()
            viewModel.resetSuccess()
        }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        viewModel.onImageSelected(uri)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Tạo ý kiến mới",
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { viewModel.submitFeedback() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeHighlight),
                    shape = RoundedCornerShape(27.dp),
                    enabled = !uiState.isSubmitting
                ) {
                    if (uiState.isSubmitting) {
                        CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(
                            text = "Gửi ý kiến",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        },
        containerColor = BackgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Đóng góp ý kiến",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Tiêu đề
                Text(text = "Tiêu đề", fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.title,
                    onValueChange = { viewModel.onTitleChange(it) },
                    placeholder = { Text("Nhập tiêu đề", color = TextGray) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Căn hộ
                FeedbackRow(label = "Căn hộ", value = uiState.apartment)
                
                // Loại phản ánh
                FeedbackRow(label = "Loại phản ánh", value = uiState.feedbackType, isClickable = true)

                Spacer(modifier = Modifier.height(16.dp))

                // Thêm ảnh hiện trạng
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Thêm ảnh hiện trạng", fontSize = 14.sp, color = Color.Black)
                    IconButton(onClick = { launcher.launch("image/*") }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Image", tint = PrimaryBlue)
                    }
                }

                if (uiState.imageUri != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .clip(RoundedCornerShape(12.dp))
                    ) {
                        Image(
                            painter = rememberAsyncImagePainter(uiState.imageUri),
                            contentDescription = "Current State",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .size(24.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.5f))
                                .clickable { viewModel.onImageSelected(null) },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove Image",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nội dung
                Text(text = "Nội dung", fontSize = 14.sp, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = uiState.content,
                    onValueChange = { viewModel.onContentChange(it) },
                    placeholder = { Text("Nhập nội dung", color = TextGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@Composable
private fun FeedbackRow(
    label: String,
    value: String,
    isClickable: Boolean = false
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = label, fontSize = 14.sp, color = TextGray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                if (isClickable) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null,
                        tint = TextGray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
        HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
    }
}

@Preview(showBackground = true)
@Composable
fun TaoYKienMoiScreenPreview() {
    BMISTheme {
        TaoYKienMoiScreen()
    }
}
