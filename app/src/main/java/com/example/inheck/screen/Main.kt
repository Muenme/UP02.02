package com.example.inheck.screen
import android.annotation.SuppressLint
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
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
//import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inheck.data.entity.Buy
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Main(
    onNavigateToEditBuy: () -> Unit,
    onNavigateToReadBuy: () -> Unit,
    purchases: List<Buy>
){
    Scaffold (
        floatingActionButton = {
            FloatingActionButton (
                onClick =  onNavigateToEditBuy,
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
            for (pur in purchases){
                item {
                    ButtonBuy(onNavigateToReadBuy, pur.date, pur.numberParticipants, pur.amount)
                }
            }

        }
    }
}
@Composable
fun ButtonBuy(
    onNavigateToReadBuy: () -> Unit,
    date: LocalDateTime,
    numberParticipants: Int,
    amount: Double
){
    Button(
        onClick = onNavigateToReadBuy,
        modifier = Modifier
            .fillMaxWidth()
            .padding(3.dp),
//        border = BorderStroke(2.dp, Color(0xFF4d4d4d)),
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF0D47A1)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 13.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ){
                Box {
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern("Дата dd.MM.uuuu")).toString(),
                        fontSize = 18.sp
                    )
                }
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                ){
                    Text(
                        text = date.format(DateTimeFormatter.ofPattern(" Время HH:mm")).toString(),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Right
                    )
                }

            }

            Text(
                text = "Количество участников "+numberParticipants.toString(),
                fontSize = 16.sp
            )
            Text(
                text = "Сумма "+amount.toString(),
                fontSize = 16.sp
            )
        }
    }
}

@Preview
@Composable
fun PreviewMain(){
    Main(
        onNavigateToEditBuy = {},
        onNavigateToReadBuy =  {},
        purchases = listOf()
    )
}


