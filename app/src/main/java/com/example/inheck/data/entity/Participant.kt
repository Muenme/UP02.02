package com.example.inheck.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("participants")
data class Participant(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val check: String
)