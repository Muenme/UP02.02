package com.example.inheck.navigation

import android.window.SplashScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inheck.data.entity.ConditionItem
import com.example.inheck.data.entity.Participant
import com.example.inheck.data.entity.Product
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
                onBackClick = { navController.navigateUp() },
                onEditClick = {navController.navigate(Screen.EditBuy.route)},
                date = "24.05.2026 14:30",
                numberParticipants = 2,
                participants = listOf(
                    Participant(id = 0, name = "Аня", check = ""),
                    Participant(id = 1, name = "Боря", check = "")
                ),
                products = listOf(
                    Product(
                        title = "Молоко",
                        price = 85.50,
                        quantity = 2,
                        condition = listOf(
                            ConditionItem("Аня", true),
                            ConditionItem("Боря", false)
                        )
                    )
                )
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