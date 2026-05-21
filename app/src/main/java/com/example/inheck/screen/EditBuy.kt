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
import com.example.inheck.data.entity.ConditionItem
import com.example.inheck.data.entity.Product

@Composable
fun ProductRow(
    product: MutableState<Product>,
    participants: List<Participant>
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp, horizontal = 4.dp),
            //verticalAlignment = Alignment.CenterVertically
        ){
            TextField(
                value = product.value.title,
                onValueChange = { product.value = product.value.copy(title = it) },
                placeholder = { Text("Название") },
                modifier = Modifier.width(100.dp).height(100.dp)
            )
            TextField(
                value = product.value.quantity.toString(),
                onValueChange = {newValue ->
                    val parsedValue = newValue.toInt()
                    product.value = product.value.copy(quantity = parsedValue) },
                placeholder = { Text("Количество") },
                modifier = Modifier.width(100.dp).height(100.dp)
            )
            TextField(
                value = product.value.quantity.toString(),
                onValueChange = {newValue ->
                    val parsedValue = newValue.toDouble()
                    product.value = product.value.copy(price = parsedValue) },
                placeholder = { Text("Цена") },
                modifier = Modifier.width(100.dp).height(100.dp)
            )
        }


        // Отобразить условия (участники + чекбоксы)
        val currentConditions = product.value.condition.toMutableList()
        participants.forEach { participant ->
            val index = currentConditions.indexOfFirst { it.participantName == participant.name }
            val conditionItem = if (index >= 0) currentConditions[index] else ConditionItem(
                participant.name,
                false
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(participant.name, modifier = Modifier.weight(1f))
                Checkbox(
                    checked = conditionItem.isChecked,
                    onCheckedChange = { isChecked ->
                        val updatedCondition = conditionItem.copy(isChecked = isChecked)
                        if (index >= 0) {
                            currentConditions[index] = updatedCondition
                        } else {
                            currentConditions.add(updatedCondition)
                        }
                        product.value = product.value.copy(condition = currentConditions)
                    }
                )
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBuy(
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    title: String,
    participants: List<Participant>
) {


    var date by remember { mutableStateOf(LocalDateTime.now()) }
    var numberParticipants by remember { mutableStateOf(1) }
    var products by remember {  mutableStateOf(mutableStateListOf<Product>())}

    products.add(Product(
        title = "", condition = participants.map { ConditionItem(it.name, false) }, price = 0.0, quantity = 0
    ))

    var isLoading by remember { mutableStateOf(false) }


    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                {Text(title, fontSize = 20.sp, fontWeight = FontWeight.Bold)},
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFF69B4),
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
                modifier = Modifier.fillMaxWidth()
            )

            // Таблица товаров
            // Заголовки
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Название", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Количество", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Цена", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)
                Text("Условие", modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold)

            }

            // Список товаров
            products.forEach { product ->
                val productState = remember { mutableStateOf(product) }
                ProductRow(product = productState, participants = participants)
            }

            // Кнопка рассчитать или сохранить
            Button(
                onClick = {
                    // Тут  реализовать логику подсчета или сохранения
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Рассчитать")
            }
        }
    }

}


// Модель товара
data class ProductItem(
    var name: String = "",
    var participantSelected: String = "",
    var isChecked: Boolean = false
)

