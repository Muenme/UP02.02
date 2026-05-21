package com.example.inheck.screen

import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.inheck.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Main(
    onNavigateToEditBuy: () -> Unit,
    onNavigateToReadBuy: () -> Unit,
){
    Scaffold (
        floatingActionButton = {
            FloatingActionButton (
                onClick = onNavigateToEditBuy,
                containerColor = Color(0xFFf7f7f7),
                contentColor = Color(0xFF4d4d4d),
                modifier = Modifier
                    .border(
                        color = Color(0xFF4d4d4d),
                        shape = RoundedCornerShape(16.dp),
                        width = 2.dp
                    )

            ){
                Icon(Icons.Default.Add, contentDescription = "Add")
            }

        }
    )
    {paddingValues ->
        LazyColumn (
            modifier = Modifier
                .padding(paddingValues)
        ) {
            item {
                Button(onClick = onNavigateToReadBuy) {
                    Text("Перейти к ReadBuy")
                }
            }
        }
    }
}
@Composable
fun ButtonBuy(

){}

@Preview
@Composable
fun PreviewMain(){
    Main(
        onNavigateToEditBuy = {},
        onNavigateToReadBuy =  {},
    )
}