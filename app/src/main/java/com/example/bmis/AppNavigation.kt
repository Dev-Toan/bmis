package com.example.bmis

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bmis.data.api.ApiService
import com.example.bmis.data.repository.AuthRepository
import com.example.bmis.data.repository.NewsRepository
import com.example.bmis.ui.screens.home.HomeScreen
import com.example.bmis.ui.screens.login.LoginScreen
import com.example.bmis.ui.screens.password.PasswordScreen
import com.example.bmis.ui.screens.News_detail.NewsDetailScreen
import com.example.bmis.ui.screens.services.ServicesScreen
import com.example.bmis.ui.screens.services.PaidServicesScreen
import com.example.bmis.ui.components.MainBottomBar
import com.example.bmis.utils.RetrofitClient
import androidx.compose.foundation.layout.WindowInsets

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Khởi tạo repositories
    val apiService: ApiService = RetrofitClient.apiService
    val authRepository = remember { AuthRepository(apiService) }
    val newsRepository = remember { NewsRepository(apiService) }

    val showBottomBar = currentRoute?.startsWith("home") == true ||
            currentRoute == "members" ||
            currentRoute == "wallet" ||
            currentRoute == "profile" ||
            currentRoute == "services" ||
            currentRoute == "DaThanhToan"

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                MainBottomBar(navController, currentRoute)
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            NavHost(navController = navController, startDestination = "login") {
                composable("login") {
                    LoginScreen(
                        onNavigateToPassword = { userName, userId ->
                            val encodedName = Uri.encode(userName)
                            navController.navigate("password/$encodedName/$userId")
                        }
                    )
                }

                composable(
                    route = "registration_detail/{statusType}",
                    arguments = listOf(
                        navArgument("statusType") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val statusTypeStr = backStackEntry.arguments?.getString("statusType") ?: "SUCCESS"
                    // Chuyển String về Enum
                    val statusType = com.example.bmis.ui.screens.services.StatusType.valueOf(statusTypeStr)

                    PaidServicesScreen(
                        statusType = statusType,
                        onBackClick = { navController.popBackStack() }
                    )
                }


                composable(
                    route = "password/{userName}/{userId}",
                    arguments = listOf(
                        navArgument("userName") { type = NavType.StringType },
                        navArgument("userId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val userName = backStackEntry.arguments?.getString("userName") ?: ""
                    val userId = backStackEntry.arguments?.getInt("userId") ?: 0

                    PasswordScreen(
                        userName = userName,
                        userId = userId,
                        onBack = { navController.popBackStack() },
                        onLoginSuccess = {
                            val encodedName = Uri.encode(userName)
                            navController.navigate("home/$encodedName") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    )
                }

                composable(
                    route = "home/{userName}",
                    arguments = listOf(navArgument("userName") { type = NavType.StringType })
                ) { backStackEntry ->
                    val userName = backStackEntry.arguments?.getString("userName") ?: ""
                    HomeScreen(
                        userName = userName,
                        onNewsClick = { post ->
                            val encodedTitle = Uri.encode(post.title)
                            val encodedContent = Uri.encode(post.body)
                            navController.navigate("news_detail/$encodedTitle/$encodedContent")
                        },
                        onServicesClick = {
                            navController.navigate("services")
                        }
                    )
                }

                composable("services") {
                    ServicesScreen(
                        initialTabIndex = 0,
                        onBackClick = { navController.popBackStack() },
                        onStatusClick = { status ->
                            navController.navigate("registration_detail/${status.statusType.name}")
                        }
                    )
                }

                composable("paid_services") {
                    PaidServicesScreen(onBackClick = { navController.popBackStack() })
                }

                composable(
                    route = "news_detail/{newsTitle}/{newsContent}",
                    arguments = listOf(
                        navArgument("newsTitle") { type = NavType.StringType },
                        navArgument("newsContent") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val newsTitle = backStackEntry.arguments?.getString("newsTitle") ?: ""
                    val newsContent = backStackEntry.arguments?.getString("newsContent") ?: ""
                    NewsDetailScreen(
                        title = newsTitle,
                        content = newsContent,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

