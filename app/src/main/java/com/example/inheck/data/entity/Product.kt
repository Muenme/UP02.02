package com.example.inheck.data.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity("products")
data class Product(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val price: Double,
    val quantity: Int,
    val condition: List<ConditionItem>
)

data class ConditionItem(
    val participantName: String,
    val isChecked: Boolean
)