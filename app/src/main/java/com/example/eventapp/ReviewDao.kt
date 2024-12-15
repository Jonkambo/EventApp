package com.example.eventapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ReviewDao {
    @Insert
    fun insert(review: Review)

    @Query("SELECT * FROM reviews")
    fun getAllReviews(): List<Review>
}