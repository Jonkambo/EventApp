package com.example.eventapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review: Review)

    @Query("SELECT * FROM reviews")
    suspend fun getAllReviews(): List<Review>

    @Query("SELECT AVG(rating) FROM reviews")
    suspend fun getAverageRating(): Float?
}