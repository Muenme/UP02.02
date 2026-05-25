package com.example.inheck.OCR

import android.content.Context
import android.net.Uri
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.IOException

class ReceiptScanner(private val context: Context) {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun scanReceipt(
        imageUri: Uri,
        onSuccess: (ReceiptData) -> Unit,
        onError: (Exception) -> Unit
    ) {
        try {
            val image = InputImage.fromFilePath(context, imageUri)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val receiptData = parseReceiptText(visionText.text)
                    onSuccess(receiptData)
                }
                .addOnFailureListener { e ->
                    onError(e)
                }
        } catch (e: IOException) {
            onError(e)
        }
    }

    private fun parseReceiptText(text: String): ReceiptData {
        val items = mutableListOf<ReceiptItem>()
        val lines = text.split("\n").map { it.trim() }

        val pricePattern = Regex("""(\d+[.,]\d{2})""")
        val quantityPattern = Regex("""(\d+[.,]?\d*)\s*[хxXх*]\s*(\d+[.,]\d{2})""")

        var i = 0
        while (i < lines.size) {
            val line = lines[i]

            if (isServiceLine(line)) {
                i++
                continue
            }

            // Товар с количеством (например: "Молоко 2 х 85.50")
            val quantityMatch = quantityPattern.find(line)
            if (quantityMatch != null) {
                val quantity = quantityMatch.groupValues[1].replace(",", ".").toDoubleOrNull() ?: 1.0
                val price = quantityMatch.groupValues[2].replace(",", ".").toDoubleOrNull() ?: 0.0
                val name = line.substringBefore(quantityMatch.value).trim()

                if (name.isNotEmpty() && price > 0) {
                    items.add(ReceiptItem(name, quantity, price))
                }
                i++
                continue
            }

            // Обычный товар (название и цена в одной строке)
            val prices = pricePattern.findAll(line).map {
                it.value.replace(",", ".").toDouble()
            }.toList()

            if (prices.isNotEmpty()) {
                val name = line.replace(pricePattern, "").trim()
                    .replace(Regex("""[*хxX]"""), "")
                    .trim()

                if (name.isNotEmpty() && name.length > 2) {
                    val price = prices.last()
                    items.add(ReceiptItem(name, 1.0, price))
                }
            }

            i++
        }

        return ReceiptData(
            items = items.distinctBy { it.name },
            rawText = text
        )
    }

    private fun isServiceLine(line: String): Boolean {
        val serviceKeywords = listOf(
            "итого", "сумма", "скидка", "касса", "кассир",
            "чек", "дата", "время", "спасибо", "сдача",
            "налог", "ндс", "***", "---", "===",
            "total", "sum", "discount"
        )
        return serviceKeywords.any { line.lowercase().contains(it) }
    }
}