package com.example.inheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.inheck.data.entity.Participant
import com.example.inheck.screen.EditBuy
import com.example.inheck.ui.theme.InСheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Создаем состояние для отображения экрана
            var showEditBuy by remember { mutableStateOf(true) }

            if (showEditBuy) {
                // Передаете колбэки для выхода или сохранения
                EditBuy(
                    onSaveClick = {
                        // Обработка сохранения
                        showEditBuy = false // или другое действие
                    },
                    onBackClick = {
                        showEditBuy = false
                    },
                    title = "Редактировать покупку",
                    participants = listOf(
                        Participant(id = 0, name = "Аня", check = ""),
                        Participant(id = 1, name = "Боря", "")
                    )
                )
            } else {
                // Можно отображать другой экран
                // Например, список покупок или главный экран
            }
        }
    }
}