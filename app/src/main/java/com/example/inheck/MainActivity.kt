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
<<<<<<< HEAD
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.inheck.navigation.MainApp
=======
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.inheck.data.entity.Participant
import com.example.inheck.screen.EditBuy
>>>>>>> master
import com.example.inheck.ui.theme.InСheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
<<<<<<< HEAD
        enableEdgeToEdge()
        setContent {
            InСheckTheme {
                MainApp(navController = rememberNavController())
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InСheckTheme {
        MainApp(navController = rememberNavController())
    }
=======
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
>>>>>>> master
}