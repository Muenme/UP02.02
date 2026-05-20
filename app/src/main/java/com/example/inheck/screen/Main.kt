package com.example.inheck.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.inheck.navigation.Screen

@Composable
fun Main(
    onNavigateToEditBuy: () -> Unit,
    onNavigateToReadBuy: () -> Unit,
    onNavigateToEditProduct: () -> Unit
){
    Column {
        Text("Главная страница")

        Button(onClick = onNavigateToEditBuy) {
            Text("Перейти к EditBuy")
        }

        Button(onClick = onNavigateToReadBuy) {
            Text("Перейти к ReadBuy")
        }

        Button(onClick = onNavigateToEditProduct) {
            Text("Перейти к EditProduct")
        }
    }
}