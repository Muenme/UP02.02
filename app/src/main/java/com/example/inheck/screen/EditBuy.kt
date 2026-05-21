package com.example.inheck.screen
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.inheck.data.entity.Buy

@Composable
fun EditBuy(
    onBack: () -> Unit,
    toEditProduct: () -> Unit,
)
{
    Column {
        Text("Страница редактирования покупки")
        Button(
            onClick = onBack
        ) {
            Text("Вернуться")
        }
        Button(
            onClick = toEditProduct
        ) {
            Text("Редактировать товар")
        }
    }

}