package com.example.inheck.navigation

import android.window.SplashScreen
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.inheck.data.entity.Buy
import com.example.inheck.screen.EditBuy
import com.example.inheck.screen.EditProduct
import com.example.inheck.screen.Main
import com.example.inheck.screen.ReadBuy
import com.example.inheck.screen.SplashScreen
import java.time.LocalDateTime
import java.util.Date

@ExperimentalMaterial3Api
@Composable
fun AppNavigation(
    navController: NavHostController
) {

    var test1: Buy = Buy(
        id = 0,
        date = LocalDateTime.now(),
        numberParticipants = 2,
        productId = 0,
        participantId = 0,
        amount = 120.0
    )
    var test2: Buy = Buy(
        id = 0,
        date = LocalDateTime.now(),
        numberParticipants = 5,
        productId = 0,
        participantId = 0,
        amount = 100000.0
    )

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.Main.route) {
            Main(
                onNavigateToEditBuy = {navController.navigate(Screen.EditBuy.route)},
                onNavigateToReadBuy =  {navController.navigate(Screen.ReadBuy.route)},
                purchases = listOf(test1, test2)
            )
        }
        composable(Screen.EditBuy.route) {
            EditBuy(
                onBack = { navController.navigateUp() },
                toEditProduct = {navController.navigate(Screen.EditProduct.route)}
            )
        }
        composable(Screen.ReadBuy.route) {
            ReadBuy(
                onBack = { navController.navigateUp() },
                toEditBuy = {navController.navigate(Screen.EditBuy.route)}
            )
        }
        composable(Screen.EditProduct.route){
            EditProduct(onBack = { navController.navigateUp() })
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