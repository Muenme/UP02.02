package com.example.inheck.screen

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.example.inheck.data.entity.ConditionItem
import com.example.inheck.data.entity.Participant
import com.example.inheck.data.entity.Product
import java.io.IOException

class ReceiptScanner(private val context: Context) {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun scanReceipt(
        imageUri: Uri,
        participants: List<Participant>,
        onSuccess: (List<Product>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val image = InputImage.fromFilePath(context, imageUri)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val products = parseReceiptText(visionText.text, participants)
                    onSuccess(products)
                }
                .addOnFailureListener { e ->
                    onError(e)
                }
        } catch (e: IOException) {
            onError(e)
        }
    }

    private fun parseReceiptText(
        text: String,
        participants: List<Participant>
    ): List<Product> {

        val products = mutableListOf<Product>()
        val lines = text.split("\n").map { it.trim() }.filter { it.isNotEmpty() }

        // Паттерны
        val pricePattern = Regex("""(\d+)[.,](\d{2})""")
        val quantityPattern = Regex("""(\d+[.,]?\d*)\s*[хxXх*×]\s*(\d+[.,]\d{2})""")

        // Стоп-слова — эти строки пропускаем
        val stopWords = listOf(
            "итого", "сумма", "скидка", "касса", "кассир",
            "чек", "дата", "время", "спасибо", "сдача",
            "налог", "ндс", "инн", "огрн", "фн", "фд", "фп",
            "наличными", "безналичными", "подытог", "принято",
            "перекрёсток", "перекресток", "пятёрочка", "пятерочка",
            "горячая", "кассовый", "магнит", "итог", "округление",
            "скккой", "цена", "кол-во"
        )

        var i = 0
        while (i < lines.size) {
            val line = lines[i]

            // Пропускаем стоп-слова
            if (stopWords.any { line.lowercase().contains(it) }) {
                i++
                continue
            }

            // Формат Перекрёстка: "1: 4112685 SM.MED.Салфетки дезинфицир.20"
            // следующая строка: "57.90    57.90 *    1    57.90"
            val crossroadPattern = Regex("""^\d+:\s*\d+\s+(.+)$""")
            val crossroadMatch = crossroadPattern.find(line)

            if (crossroadMatch != null) {
                val name = crossroadMatch.groupValues[1].trim()
                val nextLine = lines.getOrNull(i + 1) ?: ""

                // Ищем количество и цену в следующей строке
                val numbers = pricePattern.findAll(nextLine).map {
                    it.value.replace(",", ".").toDouble()
                }.toList()

                val quantityInNext = Regex("""\s+(\d+)\s+""").find(nextLine)
                val qty = quantityInNext?.groupValues?.get(1)?.toIntOrNull() ?: 1
                val price = numbers.firstOrNull() ?: 0.0

                if (name.isNotEmpty() && price > 0) {
                    products.add(
                        Product(
                            title = name,
                            price = price,
                            quantity = qty,
                            condition = participants.map { ConditionItem(it.name, false) }
                        )
                    )
                    i += 2 // пропускаем следующую строку с ценами
                    continue
                }
            }

            // Формат с количеством в строке: "Молоко 2 х 85.50"
            val qtyMatch = quantityPattern.find(line)
            if (qtyMatch != null) {
                val qty = qtyMatch.groupValues[1].replace(",", ".").toDoubleOrNull()?.toInt() ?: 1
                val price = qtyMatch.groupValues[2].replace(",", ".").toDoubleOrNull() ?: 0.0
                val name = line.substringBefore(qtyMatch.value).trim()

                if (name.isNotEmpty() && price > 0) {
                    products.add(
                        Product(
                            title = name,
                            price = price,
                            quantity = qty,
                            condition = participants.map { ConditionItem(it.name, false) }
                        )
                    )
                    i++
                    continue
                }
            }

            // Обычный формат: "Хлеб белый    45.00"
            val prices = pricePattern.findAll(line).map {
                it.value.replace(",", ".").toDouble()
            }.toList()

            if (prices.isNotEmpty()) {
                val name = line.replace(pricePattern, "")
                    .replace(Regex("""[*хxXх×]"""), "")
                    .trim()

                if (name.length > 2 && !name.matches(Regex("""\d+"""))) {
                    products.add(
                        Product(
                            title = name,
                            price = prices.last(),
                            quantity = 1,
                            condition = participants.map { ConditionItem(it.name, false) }
                        )
                    )
                }
            }

            i++
        }

        return products
    }
}