package com.example.inheck.screen

import android.os.Build
import android.widget.TableLayout
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.inheck.data.entity.Buy
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import android.os.Bundle
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Alignment
import com.example.inheck.data.entity.Participant
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import com.example.inheck.data.entity.ConditionItem
import com.example.inheck.data.entity.Product
import androidx.compose.material3.AlertDialog
import java.time.format.DateTimeFormatter
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBuy(
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    title: String,
    participants: List<Participant>,
    initialProducts: List<Product> = emptyList()
) {
    val customFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

    var date by remember { mutableStateOf(LocalDateTime.now().format(customFormatter)) }
    var numberParticipants by remember { mutableStateOf(1) }

    // Инициализируем список: если пришёл не пустой – берём его, иначе – один пустой продукт
    var products = remember {
        mutableStateListOf<Product>().apply {
            if (initialProducts.isEmpty()) {
                add(Product(
                    title = "",
                    condition = participants.map { ConditionItem(it.name, false) },
                    price = 0.0,
                    quantity = 0
                ))
            } else {
                addAll(initialProducts)
            }
        }
    }

    var isLoading by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                { Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0D47A1),
                    titleContentColor = Color.White
                ),
                actions = {
                    TextButton(
                        onClick = { onSaveClick() },
                        enabled = !isLoading
                    ) {
                        Text(
                            if (isLoading) "Сохранение..." else "Сохранить",
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }
                    TextButton(
                        onClick = { onBackClick() },
                        enabled = true
                    ) {
                        Text("Отмена", color = Color.White, fontSize = 16.sp)
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
            Text(
                text = "Дата: ${date.toString()}",
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
                //verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = numberParticipants.toString(),
                    onValueChange = {
                        val num = it.toIntOrNull() ?: 1
                        numberParticipants = num
                    },
                    label = { Text("Количество участников") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.width(200.dp)
                )
                TextButton(onClick = { /* логика фото */ }) {
                    Text("+Добавить фото", fontSize = 16.sp)
                }
            }

            // Таблица товаров
            Row(
                modifier = Modifier.fillMaxWidth().padding(all = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Text("Название", modifier = Modifier.width(100.dp).height(40.dp), fontWeight = FontWeight.Bold)
                Text("Кол-во", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Цена", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Условие", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)

            }

            products.forEachIndexed { index, product ->
                ProductRow(
                    product = product,
                    index = index,
                    onUpdate = { idx, updatedProduct -> products[idx] = updatedProduct },
                    participants = participants
                )
            }

            // Кнопка "Добавить товар" слева внизу
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                TextButton(
                    onClick = {
                        products.add(Product(
                            title = "",
                            condition = participants.map { ConditionItem(it.name, false) },
                            price = 0.0,
                            quantity = 0
                        ))
                    }
                ) {
                    Text("+Добавить товар", fontSize = 16.sp)
                }
            }


            Button(
                onClick = {
                    // Тут  реализовать логику подсчета
                },
                modifier = Modifier.fillMaxWidth(),

                ) {
                Text("Рассчитать")
            }
        }
    }
}

@Composable
fun ProductRow(
    product: Product,
    index: Int,
    onUpdate: (Int, Product) -> Unit,
    participants: List<Participant>
) {
    val openDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(all = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        OutlinedTextField(
            value = product.title,
            onValueChange = {
//                if (product != null)
//                {
//
//                }
//                else
//                {
                onUpdate(index, product.copy(title = it))
//                }
            },
            modifier = Modifier.width(100.dp).height(60.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next)

        )
        OutlinedTextField(
            value = product.quantity.toString(),
            onValueChange = { newValue ->
                val qty = newValue.toInt()
                onUpdate(index, product.copy(quantity = qty))
            },
            modifier = Modifier.width(50.dp).height(60.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),

            )
        OutlinedTextField(
            value = product.price.toString(),
            onValueChange = { newValue ->
                val price = newValue.toDoubleOrNull()
                onUpdate(index, product.copy(price = price ?: 0.0))
            },
            modifier = Modifier.width(70.dp).height(60.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),

            )
        Button(onClick = { openDialog.value = true }) {
            Text("Усл.", fontSize = 14.sp)
        }

        // Диалог условий внутри Row
        if (openDialog.value) {
            val currentConditions = product.condition.toMutableList()

            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text("Условие для товара") },
                text = {
                    Column {
                        participants.forEach { participant ->
                            val idx = currentConditions.indexOfFirst { it.participantName == participant.name }
                            val conditionItem = if (idx >= 0) currentConditions[idx]
                            else ConditionItem(participant.name, false)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(participant.name, modifier = Modifier.weight(1f))
                                Checkbox(
                                    checked = conditionItem.isChecked,
                                    onCheckedChange = { isChecked ->
                                        val updated = conditionItem.copy(isChecked = isChecked)
                                        if (idx >= 0) currentConditions[idx] = updated
                                        else currentConditions.add(updated)
                                        onUpdate(index, product.copy(condition = currentConditions.toList()))
                                    }
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(onClick = { openDialog.value = false }) {
                        Text("ОК")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { openDialog.value = false }) {
                        Text("Отмена")
                    }
                }
            )
        }
    }
}
