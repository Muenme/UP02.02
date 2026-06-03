package com.example.inheck

import com.example.inheck.data.entity.ConditionItem
import com.example.inheck.data.entity.Product
import com.example.inheck.processing.calculatePersonalChecks
import org.junit.Test
import org.junit.Assert.*

class CalculationTest {

    //Базовые тесты

    // Тест 1
    @Test
    fun Empty_list() {
        val result = calculatePersonalChecks(emptyList())
        assertTrue(result.isEmpty())
    }

    // Тест 2
    @Test
    fun One_product_one_participant_pays_full_amount() {
        val products = listOf(
            Product(
                title = "Хлеб",
                price = 50.0,
                quantity = 2,
                condition = listOf(
                    ConditionItem("Аня", true)
                )
            )
        )

        val result = calculatePersonalChecks(products)

        assertEquals(1, result.size)
        assertEquals("Аня", result[0].participantName)
        assertEquals(100.0, result[0].total, 0.01)
    }

    // Тест 3
    @Test
    fun Two_participants_share_one_product_equally() {
        val products = listOf(
            Product(
                title = "Молоко",
                price = 100.0,
                quantity = 1,
                condition = listOf(
                    ConditionItem("Аня", true),
                    ConditionItem("Боря", true)
                )
            )
        )

        val result = calculatePersonalChecks(products)

        var  anya = result.find { it.participantName == "Аня" }
        val borya = result.find { it.participantName == "Боря" }

        assertEquals(50.0, anya?.total?:0.0, 0.01)
        assertEquals(50.0, borya?.total?:0.0, 0.01)
    }

    // Тест 4
    @Test
    fun Product_one_participant_only() {
        val products = listOf(
            Product(
                title = "Пиво",
                price = 100.0,
                quantity = 1,
                condition = listOf(
                    ConditionItem("Аня", false),
                    ConditionItem("Боря", true)
                )
            )
        )

        val result = calculatePersonalChecks(products)

        val anya = result.find { it.participantName == "Аня" }
        val borya = result.find { it.participantName == "Боря" }

        assertEquals(0.0, anya?.total ?: 0.0, 0.01)
        assertEquals(100.0, borya?.total?:0.0, 0.01)
    }

    // Тест 5
    @Test
    fun One_is_marked_then_no_one_pays_anyt() {
        val products = listOf(
            Product(
                title = "Хлеб",
                price = 50.0,
                quantity = 1,
                condition = listOf(
                    ConditionItem("Аня", false),
                    ConditionItem("Боря", false)
                )
            )
        )

        val result = calculatePersonalChecks(products)

        result.forEach { check ->
            assertEquals(0.0, check.total, 0.01)
        }
    }

    //Посложнее тесты

    // Тест 6
    @Test
    fun Multiple_products_different_participants() {
        val products = listOf(
            // Хлеб - только Аня
            Product(
                title = "Хлеб",
                price = 60.0,
                quantity = 1,
                condition = listOf(
                    ConditionItem("Аня", true),
                    ConditionItem("Боря", false)
                )
            ),
            // Молоко - только Боря
            Product(
                title = "Молоко",
                price = 80.0,
                quantity = 1,
                condition = listOf(
                    ConditionItem("Аня", false),
                    ConditionItem("Боря", true)
                )
            ),
            // Сок - оба
            Product(
                title = "Сок",
                price = 100.0,
                quantity = 1,
                condition = listOf(
                    ConditionItem("Аня", true),
                    ConditionItem("Боря", true)
                )
            )
        )

        val result = calculatePersonalChecks(products)

        val anya = result.find { it.participantName == "Аня" }
        val borya = result.find { it.participantName == "Боря" }

        // Аня: Хлеб(60) + Сок/2(50) = 110
        assertEquals(110.0, anya?.total?:0.0, 0.01)

        // Боря: Молоко(80) + Сок/2(50) = 130
        assertEquals(130.0, borya?.total?:0.0, 0.01)
    }

    // Тест 7
    @Test
    fun Item_with_quantity_more_than_one() {
        val products = listOf(
            Product(
                title = "Яйца",
                price = 10.0,
                quantity = 10,
                condition = listOf(
                    ConditionItem("Аня", true),
                    ConditionItem("Боря", true)
                )
            )
        )

        val result = calculatePersonalChecks(products)

        val anya = result.find { it.participantName == "Аня" }
        val borya = result.find { it.participantName == "Боря" }

        // Итого 100, делят поровну по 50
        assertEquals(50.0, anya?.total?:0.0, 0.01)
        assertEquals(50.0, borya?.total?:0.0, 0.01)
    }

    // Тест 8
    @Test
    fun Three_participants_divide_equally() {
        val products = listOf(
            Product(
                title = "Пицца",
                price = 900.0,
                quantity = 1,
                condition = listOf(
                    ConditionItem("Аня", true),
                    ConditionItem("Боря", true),
                    ConditionItem("Вася", true)
                )
            )
        )

        val result = calculatePersonalChecks(products)

        result.forEach { check ->
            assertEquals(300.0, check.total, 0.01)
        }
    }

    // Тест 9
    @Test
    fun Sum_all_receipts_equal_sum_all_goods() {
        val products = listOf(
            Product(
                title = "Хлеб",
                price = 50.0,
                quantity = 2,
                condition = listOf(
                    ConditionItem("Аня", true),
                    ConditionItem("Боря", true)
                )
            ),
            Product(
                title = "Молоко",
                price = 89.0,
                quantity = 1,
                condition = listOf(
                    ConditionItem("Аня", true),
                    ConditionItem("Боря", false)
                )
            )
        )

        val totalProducts = products.sumOf { it.price * it.quantity }
        val result = calculatePersonalChecks(products)
        val totalChecks = result.sumOf { it.total }

        assertEquals(totalProducts, totalChecks, 0.01)
    }
}