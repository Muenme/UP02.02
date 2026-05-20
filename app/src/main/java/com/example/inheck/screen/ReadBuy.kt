package com.example.inheck.screen

import android.widget.Button
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text

@Composable
fun ReadBuy(
    onBack: () -> Unit
)
{
    Text("Страница чтения покупки")
    Button(
        onClick = onBack,
    ) {
        Text("Вернуться")
    }
}