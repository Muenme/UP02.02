package com.example.inheck.screen

<<<<<<< HEAD
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
import com.example.inheck.R

import java.sql.Date

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditBuy(
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit,
    title: String
) {
    var date by remember { mutableStateOf(LocalDateTime.now()) }
    var numberParticipants by remember { mutableStateOf(0) }
    var productId by remember { mutableStateOf(0) }
    var participantId by remember { mutableStateOf(0) }
    var amount by remember { mutableStateOf(0.0) }

    var isLoading by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    fun saveBuyToFile() {
        isLoading = true
        scope.launch {
            try {
                // Создаем объект покупки
                val buy = Buy(
                    date = date,
                    numberParticipants = numberParticipants,
                    productId = productId,
                    participantId = participantId,
                    amount = amount,

                    )

                // Сохраняем в файл через ....


                // Очищаем поля
                date = null
                numberParticipants = 0
                productId = 0
                participantId = 0
                amount = 0.0

                // Вызываем колбэк и закрываем экран
                onSaveClick()
            } catch (e: Exception) {
                errorMessage = "Ошибка сохранения: ${e.message}"
                showError = true
            } finally {
                isLoading = false
            }
        }
    }


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
                        onClick = { saveBuyToFile() },
                        enabled = !isLoading
                    ) {
                        Text(
                            if (isLoading) "Сохранение..." else "Сохранить",
                            color = Color.White,
                            fontSize = 16.sp
                        )
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
            TextField(
                value = date,
                onValueChange = {
                    val it = null
                    date = it
                },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Дата создания") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF69B4)
                )
            )

            TextField(
                value = numberParticipants,
                onValueChange = { numberParticipants = it },
                placeholder = {Text("Магия (например: Огонь)") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF69B4)
                )
            )

            TableView(
                value = productId,
                onValueChange = { productId = it },
                placeholder = {Text("Товар") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF69B4)
                )
            )

            TextField(
                value = productId,
                onValueChange = { productId = it },
                placeholder = {Text("Товар") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF69B4)
                )
            )
            @Composable
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.drawable.activity_main)

            TableLayout(
                value = productId,
                onValueChange = { productId = it },
                placeholder = {Text("Товар") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFF69B4)
                )
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFE4E1))
            ) {
                Text(
                    "Все поля обязательны для заполнения",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 14.sp
                )
            }


        }
    }
}

@Composable
fun TableView(
    value: Int,
    onValueChange: () -> Unit,
    placeholder: () -> Unit,
    keyboardOptions: KeyboardOptions,
    modifier: Modifier,
    colors: TextFieldColors
) {
    TODO("Not yet implemented")
}

=======
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun EditBuy(
    onBack: () -> Unit,
    toEditProduct: () -> Unit
)
{
    Column {
        Text("Страница редактирования покупки")
        Button(
            onClick = onBack
        ) {
            Text("Вернуться")
        }
        Button(
            onClick = toEditProduct
        ) {
            Text("Редактировать товар")
        }
    }

}
>>>>>>> origin/Muenme
