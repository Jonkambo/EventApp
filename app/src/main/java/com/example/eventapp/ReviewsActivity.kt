package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReviewsActivity : AppCompatActivity() {
    private lateinit var addReviewButton: Button
    private lateinit var reviewRecyclerView: RecyclerView
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var appDatabase: AppDatabase
    private val reviews = mutableListOf<Review>()
    private lateinit var averageRatingTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(R.layout.activity_reviews)

        appDatabase = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()

        addReviewButton = findViewById(R.id.add_review_button)
        reviewRecyclerView = findViewById(R.id.reviewRecyclerView)
        averageRatingTextView = findViewById(R.id.averageRatingTextView)

        reviewRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewAdapter = ReviewAdapter(reviews)
        reviewRecyclerView.adapter = reviewAdapter

        loadReviews()

        addReviewButton.setOnClickListener {
            val intent = Intent(this@ReviewsActivity, AddReviewActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadReviews() {
        CoroutineScope(Dispatchers.IO).launch {
            val allReviews = appDatabase.reviewDao().getAllReviews()
            reviews.clear()
            reviews.addAll(allReviews)

            val averageRating = appDatabase.reviewDao().getAverageRating() ?: 0f

            launch(Dispatchers.Main) {
                reviewAdapter.notifyDataSetChanged()
                averageRatingTextView.text = "Средний рейтинг: ${averageRating.toString()}"
            }
        }
    }
}