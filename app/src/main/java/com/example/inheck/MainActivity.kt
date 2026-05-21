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
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
=======
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.inheck.navigation.MainApp
>>>>>>> origin/Muenme
import com.example.inheck.ui.theme.InСheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InСheckTheme {
<<<<<<< HEAD
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
=======
                MainApp(navController = rememberNavController())
>>>>>>> origin/Muenme
            }
        }
    }
}

<<<<<<< HEAD
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}
=======

>>>>>>> origin/Muenme

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    InСheckTheme {
<<<<<<< HEAD
        Greeting("Android")
=======
        MainApp(navController = rememberNavController())
>>>>>>> origin/Muenme
    }
}