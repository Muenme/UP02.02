package com.example.inheck.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun EditProduct(
    onBack: () -> Unit
)
{
    Text("Страница редактирования товара")
    Button(
        onClick = onBack,
    ) {
        Text("Вернуться")
    }
}