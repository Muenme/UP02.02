package com.example.inheck.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import java.sql.Date
import java.time.LocalDateTime

@Entity("buys",
    foreignKeys = [ForeignKey(
        entity = Product::class,
        parentColumns = ["id"],
        childColumns = ["productId"],
        onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Participant::class,
            parentColumns = ["id"],
            childColumns = ["participantId"],
            onDelete = ForeignKey.CASCADE
        )],
    )
data class Buy(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: LocalDateTime,
    val numberParticipants: Int,
    val productId: List<Int>,
    val participantId: Int,
    val amount: Double
)