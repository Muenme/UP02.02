package com.example.inheck.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainApp(
    navController: NavHostController
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    val currentRoute = navBackStackEntry?.destination?.route

    var selectedItem by remember { mutableStateOf<NavItem>(NavItem.Main) }

    LaunchedEffect(currentRoute) {
        selectedItem = when {
            currentRoute?.startsWith(Screen.Main.route) == true -> NavItem.Main
            currentRoute?.startsWith(Screen.EditBuy.route) == true -> NavItem.EditBuy
            currentRoute?.startsWith(Screen.ReadBuy.route) == true -> NavItem.ReadBuy
            currentRoute?.startsWith(Screen.EditProduct.route) == true -> NavItem.EditProduct
            else -> {
                selectedItem
            }
        }

    }
    AppNavigation(navController)
}