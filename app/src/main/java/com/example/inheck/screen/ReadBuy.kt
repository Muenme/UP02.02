package com.example.inheck.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.inheck.data.entity.ConditionItem
import com.example.inheck.data.entity.Participant
import com.example.inheck.data.entity.Product

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadBuy(
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    date: String,
    numberParticipants: Int,
    participants: List<Participant>,
    products: List<Product>
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Просмотр покупки", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    TextButton(onClick = onBackClick) {
                        Text("Назад", color = Color.White, fontSize = 16.sp)
                    }
                },
                actions = {
                    TextButton(onClick = onEditClick) {
                        Text("Редактировать", color = Color.White, fontSize = 16.sp)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Дата и количество участников
            Text("Дата: $date", style = MaterialTheme.typography.bodyLarge)
            Text("Количество участников: $numberParticipants", style = MaterialTheme.typography.bodyLarge)

            // Заголовки таблицы
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Название", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Кол-во", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Цена", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Условие", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
            }

            HorizontalDivider()

            products.forEach { product ->
                ReadProductRow(product = product, participants = participants)
                HorizontalDivider()
            }
        }
    }
}

@Composable
fun ReadProductRow(
    product: Product,
    participants: List<Participant>
) {
    val openDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp, horizontal = 5.dp),
        //horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = product.title,
            modifier = Modifier.weight(1f),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = product.quantity.toString(),
            modifier = Modifier.weight(1f),
            style = TextStyle(
                textIndent = TextIndent(20.sp, 0.sp)
            )
        )
        Text(
            text = String.format("%.2f", product.price),
            modifier = Modifier.weight(1f)
        )
        Button(onClick = { openDialog.value = true }) {
            Text("Усл.", fontSize = 14.sp)
        }
    }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("Условие для товара") },
            text = {
                Column {
                    participants.forEach { participant ->
                        val conditionItem = product.condition.find { it.participantName == participant.name }
                            ?: ConditionItem(participant.name, false)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(participant.name, modifier = Modifier.weight(1f))
                            Checkbox(
                                checked = conditionItem.isChecked,
                                onCheckedChange = null,
                                enabled = false
                            )
                        }
                    }
                }
            },
            confirmButton = {
                Button(onClick = { openDialog.value = false }) {
                    Text("ОК")
                }
            }
        )
    }
}