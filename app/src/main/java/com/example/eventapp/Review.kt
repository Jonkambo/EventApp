package com.example.eventapp

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(
    val reviewText: String,
    val rating: Int, // Добавляем поле для рейтинга
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)