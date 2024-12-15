package com.example.eventapp

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room

class AddReviewActivity : AppCompatActivity() {
    private lateinit var reviewEditText: EditText
    private lateinit var submitReviewButton: Button
    private lateinit var appDatabase: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(R.layout.activity_add_review)

        // Инициализация базы данных
        appDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()

        // Инициализация элементов интерфейса
        reviewEditText = findViewById(R.id.reviewEditText)
        submitReviewButton = findViewById(R.id.submitReviewButton)

        // Установка текста верхней надписи
        findViewById<TextView>(R.id.headerTextView).text = "Ваш отзыв помогает нам становиться лучше"

        submitReviewButton.setOnClickListener {
            val reviewText = reviewEditText.text.toString()
            if (reviewText.isNotEmpty()) {
                saveReview(reviewText)
            }
        }
    }

    private fun saveReview(reviewText: String) {
        // Сохранение отзыва в базу данных
        val review = Review(reviewText)
        Thread {
            appDatabase.reviewDao().insert(review)
            finish() // Закрываем активити после сохранения
        }.start()
    }
}