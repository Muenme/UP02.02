package com.example.inheck.navigation

sealed class Screen(val route: String){

    object Main: Screen("Main")
    object EditBuy: Screen("EditBuy")
    object ReadBuy: Screen("ReadBuy")
    object SplashScreen: Screen("SplashScreen")
}
sealed class NavItem(val title:String, val route: String)
{
    object Main: NavItem("Main", Screen.Main.route)

    object EditBuy: NavItem("EditBuy", Screen.EditBuy.route)
    object ReadBuy: NavItem("ReadBuy", Screen.ReadBuy.route)
    object SplashScreen: NavItem("SplashScreen", Screen.SplashScreen.route)

}