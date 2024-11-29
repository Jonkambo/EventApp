package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AreaDetailsActivity : AppCompatActivity() {
    private lateinit var db: EventAppDB
    private lateinit var eventName: String
    private lateinit var areaTitle: TextView
    private lateinit var areaDate: TextView
    private lateinit var areaPlace: TextView
    private lateinit var areaInfo: TextView
    private lateinit var detailsHeaderText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_area_details)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        eventName = intent.getStringExtra("event_title") ?: ""
        areaTitle = findViewById(R.id.areasNameTxt)
        areaDate = findViewById(R.id.areaDate)
        areaPlace = findViewById(R.id.areaPlace)
        areaInfo = findViewById(R.id.areaInfoTxt)

        detailsHeaderText = findViewById(R.id.detailsHeaderText)
        detailsHeaderText.setOnClickListener {
            val intent = Intent(this, BasicPageActivity::class.java)
            startActivity(intent)
        }

        loadAreaDetails(eventName)
    }

    private fun loadAreaDetails(areaName: String) {
        db = EventAppDB.getDB(this)
        val eventsDao = db.eventLocationDao()

        // Асинхронный запрос данных
        lifecycleScope.launch {
            try {
                val existingEvent = eventsDao.getAreaByName(areaName)
                if (existingEvent != null) {
                    areaTitle.text = existingEvent?.eventTitle
                    areaDate.text = existingEvent?.eventDate
                    areaPlace.text = existingEvent?.address
                    areaInfo.text = existingEvent?.eventInfo
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@AreaDetailsActivity, "Не нашли событие", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } catch (e: Exception) {
                Log.e("AreaDetailsActivity", "Не нашли событие: ${e.message}")
                Toast.makeText(this@AreaDetailsActivity, "Не нашли событие", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}