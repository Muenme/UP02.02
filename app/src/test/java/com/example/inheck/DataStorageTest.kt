package com.example.inheck

import com.example.inheck.data.entity.Buy
import com.example.inheck.data.entity.ConditionItem
import com.example.inheck.data.entity.Participant
import com.example.inheck.data.entity.Product
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.junit.Test
import org.junit.Assert.*
import java.time.LocalDateTime

class DataStorageTest {

    private val gson = GsonBuilder().setPrettyPrinting().create()

    //Тесты модели

    // Тест 1
    @Test
    fun Cost_null() {
        val product = Product(
            title = "Тест",
            price = 0.0,
            quantity = 1,
            condition = emptyList()
        )

        assertEquals(0.0, product.price, 0.01)
    }

    // Тест 2
    @Test
    fun Total() {
        val product = Product(
            title = "Хлеб",
            price = 50.0,
            quantity = 3,
            condition = emptyList()
        )

        val total = product.price * product.quantity
        assertEquals(150.0, total, 0.01)
    }

    // Тест 3
    @Test
    fun Participant_null_name() {
        val participant = Participant(
            id = 1,
            name = "",
            check = ""
        )

        val json = gson.toJson(participant)
        val restored = gson.fromJson(json, Participant::class.java)

        assertEquals("", restored.name)
    }
}