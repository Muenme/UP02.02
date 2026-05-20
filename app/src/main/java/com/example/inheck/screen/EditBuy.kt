package com.example.inheck.screen

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun EditBuy(
    onBack: () -> Unit
)
{
    Text("Страница редактирования покупки")
    Button(
        onClick = onBack,
    ) {
        Text("Вернуться")
    }
}