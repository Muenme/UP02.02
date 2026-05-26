package com.example.inheck.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.inheck.data.entity.Buy
import com.example.inheck.data.entity.ConditionItem
import com.example.inheck.data.entity.Participant
import com.example.inheck.screen.EditBuy
import com.example.inheck.screen.Main
import com.example.inheck.screen.ReadBuy
import com.example.inheck.screen.SplashScreen
import com.example.inheck.data.entity.Product
import com.example.inheck.file.DataStorage

@ExperimentalMaterial3Api
@Composable
fun AppNavigation(
    navController: NavHostController
) {
    val context = LocalContext.current
    val storage = remember { DataStorage(context) }
    var buys by remember { mutableStateOf(storage.loadBuys()) }

    NavHost(navController = navController, startDestination = Screen.SplashScreen.route) {

        composable(Screen.Main.route) {
            LaunchedEffect(Unit) {
                buys = storage.loadBuys()
            }
            Main(
                onNavigateToEditBuy = {navController.navigate(Screen.EditBuy.createRoute(Screen.EditBuy.NEW_BUY))},
                onNavigateToReadBuy = { id ->
                    navController.navigate(Screen.ReadBuy.createRoute(id))
                },
                purchases = buys
            )
        }
        composable(
            route = Screen.EditBuy.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->

            val buyId = backStackEntry.arguments?.getInt("id") ?: Screen.EditBuy.NEW_BUY
            val participants = storage.loadParticipants()

            // Если id = -1 — создаём новую, иначе — редактируем существующую
            if (buyId == Screen.EditBuy.NEW_BUY) {
                // Создание новой покупки
                EditBuy(
                    onBackClick = {
                        buys = storage.loadBuys()
                        navController.navigateUp()
                    },
                    title = "Создание",
                    participants = participants,
                    initialParticipantsCount = 1,
                    initialProducts = listOf()
                )
            } else {
                // Редактирование существующей
                val buy = storage.loadBuys().find { it.id == buyId }
                val products = buy?.let { storage.getProductsForBuy(it) } ?: emptyList()

                EditBuy(
                    onBackClick = {
                        buys = storage.loadBuys()
                        navController.navigateUp()
                    },
                    title = "Редактирование",
                    participants = participants,
                    initialParticipantsCount = buy?.numberParticipants ?: 1,
                    initialProducts = products
                )
            }
        }
        composable(
            route = Screen.ReadBuy.route,
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->

            // Получаем id из маршрута
            val buyId = backStackEntry.arguments?.getInt("id") ?: 0

            // Находим нужную покупку
            val buy = storage.loadBuys().find { it.id == buyId }
            val products = buy?.let { storage.getProductsForBuy(it) } ?: emptyList()
            val participants = storage.loadParticipants()

            if (buy != null) {
                ReadBuy(
                    onBackClick = { navController.navigateUp() },
                    onEditClick = { navController.navigate(Screen.EditBuy.createRoute(buyId)) },
                    date = buy.date.toString(),
                    numberParticipants = buy.numberParticipants,
                    participants = participants,
                    products = products
                )
            }
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