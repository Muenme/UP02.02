package com.example.inheck.navigation

import android.window.SplashScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inheck.data.entity.Participant
import com.example.inheck.screen.EditBuy
import com.example.inheck.screen.Main
import com.example.inheck.screen.ReadBuy
import com.example.inheck.screen.SplashScreen

@ExperimentalMaterial3Api
@Composable
fun AppNavigation(
    navController: NavHostController
) {

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.Main.route) {
            Main(
                onNavigateToEditBuy = {navController.navigate(Screen.EditBuy.route)},
                onNavigateToReadBuy =  {navController.navigate(Screen.ReadBuy.route)}
            )
        }
        composable(Screen.EditBuy.route) {
            EditBuy(
                onBackClick = { navController.navigateUp() },
                title = "Редактировать",
                participants = listOf(
                    Participant(id = 0, name = "Аня", check = ""),
                    Participant(id = 1, name = "Боря", "")
                ),
                initialProducts = listOf()
            )
        }
        composable(Screen.ReadBuy.route) {
            ReadBuy(
                onBack = { navController.navigateUp() },
                toEditBuy = {navController.navigate(Screen.EditBuy.route)}
            )
        }

        composable(Screen.SplashScreen.route) {
            SplashScreen(
                {
                    navController.navigate(Screen.Main.route) {
                        popUpTo(Screen.Main.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}