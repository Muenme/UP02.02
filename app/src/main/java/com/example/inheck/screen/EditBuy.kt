package com.example.inheck.screen
import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.Alignment
import com.example.inheck.data.entity.Participant
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import com.example.inheck.data.entity.ConditionItem
import com.example.inheck.data.entity.Product
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import java.time.format.DateTimeFormatter
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.ui.platform.LocalContext
import com.example.inheck.file.DataStorage
import com.example.inheck.screen.ReceiptScanner
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.text.style.TextOverflow

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBuy(
    onBackClick: () -> Unit,
    title: String,
    participants: List<Participant>,
    initialParticipantsCount: Int = 1,
    initialProducts: List<Product> = emptyList()
) {
    val customFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    var date by remember { mutableStateOf(LocalDateTime.now().format(customFormatter)) }

    // Формируем начальный список участников на основе количества
    val initialParticipants = remember(participants, initialParticipantsCount) {
        if (participants.isNotEmpty()) {
            // Если передан список имён — используем его
            if (participants.size >= initialParticipantsCount) {
                participants.take(initialParticipantsCount)
            } else {
                // Дополняем недостающими "УчастникN"
                val existingNumbers = participants.mapNotNull {
                    Regex("""Участник(\d+)""").find(it.name)?.groupValues?.get(1)?.toIntOrNull()
                }
                val maxNumber = existingNumbers.maxOrNull() ?: 0
                participants.toMutableList().apply {
                    for (i in participants.size until initialParticipantsCount) {
                        add(Participant(id = i, name = "Участник${maxNumber + i - participants.size + 1}", check = ""))
                    }
                }
            }
        } else {
            // Создаём участников по умолчанию
            (0 until initialParticipantsCount).map { index ->
                Participant(id = index, name = "Участник${index + 1}", check = "")
            }
        }
    }

    // Состояние общего списка участников
    val participantsState = remember { mutableStateListOf<Participant>() }
    LaunchedEffect(initialParticipants) {
        participantsState.clear()
        participantsState.addAll(initialParticipants)
    }

    var numberParticipants by remember { mutableStateOf(initialParticipantsCount) }

    val context = LocalContext.current
    val storage = remember { DataStorage(context) }


    // Инициализируем список: если пришёл не пустой – берём его, иначе – один пустой продукт
    // Нормализуем переданные продукты
    val normalizedInitialProducts = remember(initialProducts, initialParticipants) {
        initialProducts.map { product ->
            val newCondition = initialParticipants.map { participant ->
                val existing = product.condition.find { it.participantName == participant.name }
                existing ?: ConditionItem(participant.name, false)
            }
            product.copy(condition = newCondition)
        }
    }

    // Список продуктов в состоянии
    var products = remember {
        mutableStateListOf<Product>().apply {
            if (normalizedInitialProducts.isEmpty()) {
                add(Product(
                    title = "",
                    condition = participantsState.map { ConditionItem(it.name, false) },
                    price = 0.0,
                    quantity = 0
                ))
            } else {
                addAll(normalizedInitialProducts)
            }
        }
    }

    // Синхронизация condition при изменении participantsState
    fun syncConditions() {
        for (i in products.indices) {
            val oldCondition = products[i].condition
            val newCondition = participantsState.mapIndexed { index, participant ->
                if (index < oldCondition.size) {
                    oldCondition[index].copy(participantName = participant.name)
                } else {
                    ConditionItem(participant.name, false)
                }
            }
            products[i] = products[i].copy(condition = newCondition)
        }
    }

    // Обработчик изменения количества участников
    fun updateParticipantsCount(newCount: Int) {
        val currentSize = participantsState.size
        if (newCount > currentSize) {
            val maxExistingNumber = participantsState.mapNotNull {
                Regex("""Участник(\d+)""").find(it.name)?.groupValues?.get(1)?.toIntOrNull()
            }.maxOrNull() ?: 0
            for (i in currentSize until newCount) {
                val newNumber = maxExistingNumber + (i - currentSize + 1)
                participantsState.add(Participant(id = i, name = "Участник$newNumber", check = ""))
            }
        } else if (newCount < currentSize) {
            repeat(currentSize - newCount) {
                participantsState.removeAt(participantsState.lastIndex)
            }
        }
        syncConditions()
        numberParticipants = participantsState.size
    }

    // Обработчик изменения имени участника
    fun onParticipantNameChange(index: Int, newName: String) {
        if (index in participantsState.indices) {
            val oldName = participantsState[index].name
            participantsState[index] = participantsState[index].copy(name = newName)
            for (i in products.indices) {
                val updatedCondition = products[i].condition.map { item ->
                    if (item.participantName == oldName) item.copy(participantName = newName) else item
                }
                products[i] = products[i].copy(condition = updatedCondition)
            }
        }
    }

    var isLoading by remember { mutableStateOf(false) }



    val scanner = remember { ReceiptScanner(context) }

// Launcher для выбора фото
    val photoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            isLoading = true
            scanner.scanReceipt(
                imageUri = it,
                participants = participants,
                onSuccess = { scannedProducts ->
                    // Убираем первый пустой product если он пустой
                    if (products.size == 1 &&
                        products[0].title.isEmpty() &&
                        products[0].price == 0.0
                    ) {
                        products.clear()
                    }
                    // Добавляем найденные товары
                    products.addAll(scannedProducts)
                    isLoading = false
                },
                onError = { error ->
                    isLoading = false
                }
            )
        }
    }
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
                        onClick = {
                            scope.launch {
                                isLoading = true

                                // Создаём Buy
                                val buy = Buy(
                                    date = LocalDateTime.now(),
                                    numberParticipants = numberParticipants,
                                    productId = emptyList(), // заполнится в addBuy
                                    participantId = 0, // укажите нужный id
                                    amount = products.sumOf { it.price * it.quantity }
                                )

                                // Сохраняем (products автоматически сохранятся внутри)
                                storage.addBuy(buy, products.toList())

                                isLoading = false
                                onBackClick()
                            }
                        },
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
                    onValueChange = { input ->
                        val num = input.toIntOrNull() ?: 1
                        updateParticipantsCount(num.coerceAtLeast(1))
                    },
                    label = { Text("Количество участников") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    modifier = Modifier.width(200.dp)
                )
                TextButton(
                    onClick = { photoLauncher.launch("image/*") },
                    enabled = !isLoading
                ) {
                    Text(
                        text = if (isLoading) "Загрузка..." else "+Добавить фото",
                        fontSize = 16.sp
                    )
                }
            }

            // Таблица товаров - заголовки
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 2.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Название - такая же ширина как поле
                Text(
                    "Название",
                    modifier = Modifier.width(110.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center
                )
                // Кол-во - такая же ширина как поле
                Text(
                    "Кол-во",
                    modifier = Modifier.width(55.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center

                )
                // Цена - такая же ширина как поле
                Text(
                    "Цена",
                    modifier = Modifier.width(75.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center

                )
                // Условие - такая же ширина как кнопка
                Text(
                    "Условие",
                    modifier = Modifier.width(65.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center

                )
            }

            products.forEachIndexed { index, product ->
                ProductRow(
                    product = product,
                    index = index,
                    onUpdate = { idx, updatedProduct -> products[idx] = updatedProduct },
                    participants = participantsState,
                    onParticipantNameChange = ::onParticipantNameChange
                )
            }

            // Кнопка "Добавить товар"
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                TextButton(
                    onClick = {
                        products.add(Product(
                            title = "",
                            condition = participantsState.map { ConditionItem(it.name, false) },
                            price = 0.0,
                            quantity = 0
                        ))
                    }
                ) {
                    Text("+Добавить товар", fontSize = 16.sp)
                }
            }


            Button(
                onClick = { /* логика подсчета */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Рассчитать")
            }
        }
    }
}

@Composable
fun ProductRoww(
    product: Product,
    index: Int,
    onUpdate: (Int, Product) -> Unit,
    participants: List<Participant>
) {
    val openDialog = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Название - 110.dp
        OutlinedTextField(
            value = product.title,
            onValueChange = { onUpdate(index, product.copy(title = it)) },
            modifier = Modifier
                .width(110.dp)
                .height(56.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        // Кол-во - 55.dp
        OutlinedTextField(
            value = product.quantity.toString(),
            onValueChange = { newValue ->
                val qty = newValue.toIntOrNull() ?: 0
                onUpdate(index, product.copy(quantity = qty))
            },
            modifier = Modifier
                .width(55.dp)
                .height(56.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        // Цена - 75.dp
        OutlinedTextField(
            value = product.price.toString(),
            onValueChange = { newValue ->
                val price = newValue.toDoubleOrNull()
                onUpdate(index, product.copy(price = price ?: 0.0))
            },
            modifier = Modifier
                .width(75.dp)
                .height(56.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        // Кнопка условие - 65.dp
        Button(
            onClick = { openDialog.value = true },
            modifier = Modifier.width(65.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Усл.", fontSize = 13.sp)
        }
    }

    // Диалог вынесли из Row
    if (openDialog.value) {
        val currentConditions = product.condition.toMutableList()

        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = { Text("Условие для товара") },
            text = {
                Column {
                    participants.forEach { participant ->
                        val idx = currentConditions.indexOfFirst {
                            it.participantName == participant.name
                        }
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
                                    onUpdate(
                                        index,
                                        product.copy(condition = currentConditions.toList())
                                    )
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

@Composable
fun ProductRow(
    product: Product,
    index: Int,
    onUpdate: (Int, Product) -> Unit,
    participants: List<Participant>,
    onParticipantNameChange: (Int, String) -> Unit      // колбэк изменения имени
) {
    val openDialog = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp, horizontal = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Название - 110.dp
        OutlinedTextField(
            value = product.title,
            onValueChange = { onUpdate(index, product.copy(title = it)) },
            modifier = Modifier
                .width(110.dp)
                .height(56.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        // Кол-во - 55.dp
        OutlinedTextField(
            value = product.quantity.toString(),
            onValueChange = { newValue ->
                val qty = newValue.toIntOrNull() ?: 0
                onUpdate(index, product.copy(quantity = qty))
            },
            modifier = Modifier
                .width(55.dp)
                .height(56.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            singleLine = true
        )

        // Цена - 75.dp
        OutlinedTextField(
            value = product.price.toString(),
            onValueChange = { newValue ->
                val price = newValue.toDoubleOrNull()
                onUpdate(index, product.copy(price = price ?: 0.0))
            },
            modifier = Modifier
                .width(75.dp)
                .height(56.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Decimal
            ),
            singleLine = true
        )

        // Кнопка условие - 65.dp
        Button(
            onClick = { openDialog.value = true },
            modifier = Modifier.width(65.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            Text("Усл.", fontSize = 13.sp)
        }
    }

        // Диалог условий внутри Row
        if (openDialog.value) {
            val currentConditions = product.condition.toMutableList()

            AlertDialog(
                onDismissRequest = { openDialog.value = false },
                title = { Text("Условие для товара") },
                text = {
                    Column {
                        participants.forEachIndexed { participantIndex, participant ->
                            val conditionItem = currentConditions.getOrElse(participantIndex) {
                                ConditionItem(participant.name, false)
                            }
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Редактируемое имя участника
                                OutlinedTextField(
                                    value = participant.name,
                                    onValueChange = { newName ->
                                        onParticipantNameChange(participantIndex, newName)
                                    },
                                    modifier = Modifier.weight(1f).padding(end = 8.dp),
                                    singleLine = true
                                )
                                Checkbox(
                                    checked = conditionItem.isChecked,
                                    onCheckedChange = { isChecked ->
                                        val updated = conditionItem.copy(isChecked = isChecked)
                                        if (participantIndex < currentConditions.size) {
                                            currentConditions[participantIndex] = updated
                                        } else {
                                            currentConditions.add(updated)
                                        }
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


