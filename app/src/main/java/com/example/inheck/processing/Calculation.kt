package com.example.inheck.processing

import com.example.inheck.data.entity.Product

data class ParticipantCheck(
    val participantName: String,
    val items: List<CheckItem>,
    val total: Double
) {
    fun toText(): String {
        val sb = StringBuilder()
        sb.appendLine("Чек: $participantName")
        sb.appendLine("─".repeat(30))
        if (items.isEmpty()) {
            sb.appendLine("Нет товаров")
        } else {
            items.forEach { item ->
                sb.appendLine("${item.productTitle} ×${item.quantity} — ${"%.2f".format(item.totalPrice)}")
            }
        }
        sb.appendLine("─".repeat(30))
        sb.appendLine("Итого: ${"%.2f".format(total)}")
        return sb.toString()
    }
}

data class CheckItem(
    val productTitle: String,
    val quantity: Int,
    val pricePerUnit: Double,
    val totalPrice: Double
)

fun calculatePersonalChecks(products: List<Product>): List<ParticipantCheck> {
    val allParticipants = products
        .flatMap { it.condition }
        .map { it.participantName }
        .distinct()

    return allParticipants.map { participantName ->
        val items = mutableListOf<CheckItem>()
        var total = 0.0

        for (product in products) {
            val conditionItem = product.condition.find { it.participantName == participantName }
            if (conditionItem?.isChecked == true) {
                val checkedCount = product.condition.count { it.isChecked }
                if (checkedCount > 0) {
                    val totalProductPrice = product.price * product.quantity
                    val sharePrice = totalProductPrice / checkedCount

                    items.add(
                        CheckItem(
                            productTitle = product.title,
                            quantity = product.quantity,
                            pricePerUnit = product.price,
                            totalPrice = sharePrice
                        )
                    )
                    total += sharePrice
                }
            }
        }

        ParticipantCheck(
            participantName = participantName,
            items = items,
            total = total
        )
    }
}