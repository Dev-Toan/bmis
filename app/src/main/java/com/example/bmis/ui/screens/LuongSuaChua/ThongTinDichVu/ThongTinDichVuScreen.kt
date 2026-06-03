package com.example.bmis.ui.screens.LuongSuaChua.ThongTinDichVu

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.example.bmis.R
import com.example.bmis.ui.theme.BMISTheme
import com.example.bmis.ui.theme.OrangeHighlight
import com.example.bmis.ui.theme.PrimaryBlue
import com.example.bmis.ui.theme.TextGray

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThongTinDichVuScreen(
    onBackClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    viewModel: ThongTinDichVuViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Dịch vụ sửa chữa",
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
                    onClick = onRegisterClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = OrangeHighlight),
                    shape = RoundedCornerShape(27.dp)
                ) {
                    Text(
                        text = "Đăng ký",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        },
        containerColor = Color(0xFFF4F6F9)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            // Ảnh banner chính
            Image(
                painter = painterResource(id = R.drawable.chuachay), // Thay thế bằng ảnh thợ sửa chữa phù hợp
                contentDescription = "Repair Service",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = uiState.title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiState.description,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = uiState.devicesTitle,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
                
                uiState.devices.forEach { service ->
                    Row(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
                        Text(text = "•", modifier = Modifier.padding(end = 8.dp))
                        Text(text = service, fontSize = 14.sp, color = Color.DarkGray)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = uiState.commitmentTitle,
                    fontSize = 14.sp,
                    color = Color.DarkGray
                )
                
                uiState.commitments.forEachIndexed { index, commitment ->
                    Row(modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)) {
                        Text(text = "${index + 1}.", modifier = Modifier.padding(end = 8.dp))
                        Text(text = commitment, fontSize = 14.sp, color = Color.DarkGray)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThongTinDichVuScreenPreview() {
    BMISTheme {
        ThongTinDichVuScreen()
    }
}
