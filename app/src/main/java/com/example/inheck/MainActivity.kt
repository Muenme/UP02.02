package com.example.inheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.inheck.navigation.MainApp
import com.example.inheck.ui.theme.InСheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}