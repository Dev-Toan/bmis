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
import com.example.bmis.ui.screens.ChiTietDanhSachDangKy.PaidServicesScreen
import com.example.bmis.ui.components.MainBottomBar
import com.example.bmis.ui.screens.GioHang.CartScreen
import com.example.bmis.utils.RetrofitClient
import androidx.compose.foundation.layout.WindowInsets
import com.example.bmis.ui.screens.ChiTietDichVu.ServiceDetailScreen
import com.example.bmis.ui.screens.GoiDichVuTienIch.PackageDetailScreen
import com.example.bmis.data.models.RegistrationOrderStatus
import com.example.bmis.ui.screens.ThongTinDangKy.RegistrationDetailScreen
import com.example.bmis.ui.screens.services.StatusType

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
            currentRoute == "DaThanhToan" ||
            currentRoute?.startsWith("service_detail") == true

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
                        onBackClick = { navController.popBackStack() },
                        onCartClick = { navController.navigate("cart") },
                        onItemClick = { invoice, status ->
                            val registrationStatus = when (status) {
                                com.example.bmis.ui.screens.services.StatusType.SUCCESS -> RegistrationOrderStatus.PAID
                                com.example.bmis.ui.screens.services.StatusType.WARNING -> RegistrationOrderStatus.PENDING
                                com.example.bmis.ui.screens.services.StatusType.ERROR -> RegistrationOrderStatus.CANCELLED
                            }
                            navController.navigate(
                                "registration_detail_screen/${invoice.id}/${registrationStatus.name}"
                            )
                        }
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
                            navController.navigate("news_detail/${post.id}")
                        },
                        onServicesClick = {
                            navController.navigate("services")
                        }
                    )
                }

                composable(
                    route = "service_detail/{serviceId}",
                    arguments = listOf(
                        navArgument("serviceId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "1"
                    ServiceDetailScreen(
                        serviceId = serviceId,
                        onBackClick = { navController.popBackStack() },
                        onCartClick = { navController.navigate("cart") },
                        onViewPackageClick = { pkg ->
                            navController.navigate("package_detail/$serviceId/${pkg.id}")
                        }
                    )
                }

                composable("services") {
                    ServicesScreen(
                        initialTabIndex = 0,
                        onBackClick = { navController.popBackStack() },
                        onCartClick = { navController.navigate("cart") },
                        onStatusClick = { status ->
                            navController.navigate("registration_detail/${status.statusType.name}")
                        },
                        onServiceClick = { service ->
                            navController.navigate("service_detail/${service.id}")
                        }
                    )
                }

                composable("cart") {
                    CartScreen(
                        onBackClick = { navController.popBackStack() },
                        onRegisterClick = { /* Xử lý đăng ký */ }
                    )
                }

                composable(
                    route = "registration_detail_screen/{registrationId}/{status}",
                    arguments = listOf(
                        navArgument("registrationId") { type = NavType.StringType },
                        navArgument("status") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val registrationId = backStackEntry.arguments?.getString("registrationId") ?: "0"
                    val statusStr = backStackEntry.arguments?.getString("status") ?: "PENDING"
                    val status = RegistrationOrderStatus.valueOf(statusStr)

                    RegistrationDetailScreen(
                        registrationId = registrationId,
                        status = status,
                        onBackClick = { navController.popBackStack() }
                    )
                }

                composable("paid_services") {
                    PaidServicesScreen(
                        onBackClick = { navController.popBackStack() },
                        onCartClick = { navController.navigate("cart") }
                    )
                }

                composable(
                    route = "package_detail/{serviceId}/{packageId}",
                    arguments = listOf(
                        navArgument("serviceId") { type = NavType.StringType },
                        navArgument("packageId") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val serviceId = backStackEntry.arguments?.getString("serviceId") ?: "1"
                    val packageId = backStackEntry.arguments?.getString("packageId") ?: "1"
                    PackageDetailScreen(
                        serviceId = serviceId,
                        packageId = packageId,
                        onBackClick = { navController.popBackStack() },
                        onCartClick = { navController.navigate("cart") },
                        onAddToCartClick = { navController.navigate("cart") }
                    )
                }

                composable(
                    route = "news_detail/{postId}",
                    arguments = listOf(
                        navArgument("postId") { type = NavType.IntType }
                    )
                ) { backStackEntry ->
                    val postId = backStackEntry.arguments?.getInt("postId") ?: 0
                    NewsDetailScreen(
                        postId = postId,
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}

