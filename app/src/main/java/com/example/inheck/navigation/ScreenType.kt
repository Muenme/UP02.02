package com.example.inheck.navigation

sealed class Screen(val route: String){

    object Main: Screen("Main")
    object EditBuy : Screen("edit_buy/{id}") {
        fun createRoute(id: Int) = "edit_buy/$id"
        // -1 означает создание новой покупки
        const val NEW_BUY = -1
    }
    object ReadBuy : Screen("read_buy/{id}") {
        fun createRoute(id: Int) = "read_buy/$id"
    }
    object SplashScreen: Screen("SplashScreen")

}
sealed class NavItem(val title:String, val route: String)
{
    object Main: NavItem("Main", Screen.Main.route)

    object EditBuy: NavItem("EditBuy", Screen.EditBuy.route)
    object ReadBuy: NavItem("ReadBuy", Screen.ReadBuy.route)
    object SplashScreen: NavItem("SplashScreen", Screen.SplashScreen.route)

}