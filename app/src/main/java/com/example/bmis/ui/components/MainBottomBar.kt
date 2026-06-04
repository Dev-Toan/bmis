package com.example.bmis.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.bmis.ui.theme.PrimaryBlue

@Composable
fun MainBottomBar(navController: NavHostController, currentRoute: String?) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val items = listOf(
            NavigationItem("home", "Trang chủ", Icons.Default.Home),
            NavigationItem("members", "Thành viên", Icons.Default.Group),
            NavigationItem("wallet", "Pay", Icons.Default.AccountBalanceWallet),
            NavigationItem("profile", "Cá nhân", Icons.Default.Person)
        )

        items.forEach { item ->
            val isSelected = currentRoute?.startsWith(item.route) == true
            
            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (!isSelected) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryBlue,
                    selectedTextColor = PrimaryBlue,
                    unselectedIconColor = Color.Gray,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.White
                )
            )
        }
    }
}

private data class NavigationItem(
    val route: String,
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)