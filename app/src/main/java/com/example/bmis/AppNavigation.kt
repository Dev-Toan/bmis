package com.example.bmis

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bmis.data.api.ApiService
import com.example.bmis.data.repository.AuthRepository
import com.example.bmis.data.repository.NewsRepository
import com.example.bmis.ui.screens.home.HomeScreen
import com.example.bmis.ui.screens.login.LoginScreen
import com.example.bmis.ui.screens.login.LoginViewModel
import com.example.bmis.ui.screens.password.PasswordScreen
import com.example.bmis.ui.screens.password.PasswordViewModel
import com.example.bmis.utils.RetrofitClient
import com.example.bmis.ui.screens.home.HomeViewModel

import com.example.bmis.ui.screens.News_detail.NewsDetailScreen
import com.example.bmis.ui.screens.services.ServicesScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    // Khởi tạo repositories
    val apiService: ApiService = RetrofitClient.apiService
    val authRepository = remember { AuthRepository(apiService) }
    val newsRepository = remember { NewsRepository(apiService) }

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
            ServicesScreen(onBack = { navController.popBackStack() })
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