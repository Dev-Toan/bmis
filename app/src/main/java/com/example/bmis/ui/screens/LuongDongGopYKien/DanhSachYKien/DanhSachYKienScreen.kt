package com.example.bmis.ui.screens.LuongDongGopYKien.DanhSachYKien

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bmis.data.models.FeedbackItem
import com.example.bmis.ui.theme.BMISTheme

private val PrimaryBlue = Color(0xFF0F3C88)
private val BackgroundColor = Color(0xFFF4F6F9)
private val TextGray = Color(0xFF8A92A6)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DanhSachYKienScreen(
    onBackClick: () -> Unit = {},
    onAddClick: () -> Unit = {},
    onItemClick: (FeedbackItem) -> Unit = {},
    viewModel: DanhSachYKienViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Đóng góp ý kiến",
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
                    IconButton(onClick = onAddClick) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color.White)
        ) {
            items(uiState.feedbackList) { item ->
                FeedbackItemRow(
                    item = item,
                    onClick = { onItemClick(item) }
                )
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    thickness = 0.5.dp,
                    color = Color.LightGray.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Composable
private fun FeedbackItemRow(item: FeedbackItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Text(
            text = item.title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(8.dp))
        
        FeedbackDetailRow(label = "Căn hộ", value = item.apartment)
        FeedbackDetailRow(label = "Loại", value = item.type)
        FeedbackDetailRow(label = "Nội dung", value = item.content)
        
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Tình trạng: ",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.width(100.dp)
            )
            Text(
                text = item.status.title,
                color = item.status.color,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        FeedbackDetailRow(label = "Thời gian", value = item.date)
    }
}

@Composable
private fun FeedbackDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "$label: ",
            color = Color.Gray,
            fontSize = 14.sp,
            modifier = Modifier.width(100.dp)
        )
        Text(
            text = value,
            color = Color.DarkGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DanhSachYKienScreenPreview() {
    BMISTheme {
        DanhSachYKienScreen()
    }
}
