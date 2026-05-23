package com.example.inheck.OCR

data class ReceiptItem(
    val name: String,
    val quantity: Double = 1.0,
    val price: Double,
    val total: Double = price * quantity
)

data class ReceiptData(
    val items: List<ReceiptItem>,
    val totalSum: Double = items.sumOf { it.total },
    val rawText: String
)