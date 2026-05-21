package com.example.inheck.screen

<<<<<<< HEAD
=======
import android.widget.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.material3.Text

@Composable
fun ReadBuy(
    onBack: () -> Unit,
    toEditBuy: () -> Unit
)
{
    Column {
        Text("Страница чтения покупки")
        Button(
            onClick = onBack
        ) {
            Text("Вернуться")
        }
        Button(
            onClick = toEditBuy
        ){
            Text("Редактировать")
        }
    }

}
>>>>>>> origin/Muenme
