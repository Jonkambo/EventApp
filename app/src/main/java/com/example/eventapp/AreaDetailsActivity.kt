package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.lifecycleScope
import com.example.eventapp.Data.getUserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString

class AreaDetailsActivity : AppCompatActivity() {
    private lateinit var db: EventAppDB
    private lateinit var eventName: String
    private lateinit var areaTitle: TextView
    private lateinit var areaDate: TextView
    private lateinit var areaPlace: TextView
    private lateinit var areaInfo: TextView
    private lateinit var areaButton: Button
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
        areaButton = findViewById(R.id.signForEvent)

        detailsHeaderText = findViewById(R.id.detailsHeaderText)
        detailsHeaderText.setOnClickListener {
            val intent = Intent(this, BasicPageActivity::class.java)
            startActivity(intent)
        }

        loadAreaDetails(eventName)
    }

    private fun setupRegisterButton(eventTitle: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val userId = getUserId(this@AreaDetailsActivity)
            val userDao = EventAppDB.getDB(this@AreaDetailsActivity).userDao()
            val user = userDao.getUserById(userId)

            val isRegistered = user?.areas?.let {
                kotlinx.serialization.json.Json.decodeFromString<List<String>>(it)
            }?.contains(eventTitle) == true

            withContext(Dispatchers.Main) {
                updateRegisterButton(isRegistered, eventTitle)
            }
        }
    }

    private fun updateRegisterButton(isRegistered: Boolean, eventTitle: String) {
        if (isRegistered) {
            areaButton.text = "Убрать запись"
            areaButton.setOnClickListener { unregisterFromEvent(eventTitle) }
        } else {
            areaButton.text = "Записаться"
            areaButton.setOnClickListener { registerForEvent(eventTitle) }
        }
    }

    private fun registerForEvent(eventTitle: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val userDao = EventAppDB.getDB(this@AreaDetailsActivity).userDao()
            val userId = getUserId(this@AreaDetailsActivity)

            val user = userDao.getUserById(userId)
            val updatedAreas = user?.areas?.let {
                val areas = kotlinx.serialization.json.Json.decodeFromString<MutableList<String>>(it)
                areas.add(eventTitle)
                kotlinx.serialization.json.Json.encodeToString(areas)
            } ?: kotlinx.serialization.json.Json.encodeToString(listOf(eventTitle))

            userDao.updateUserAreas(userId, updatedAreas)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@AreaDetailsActivity, "Вы записались на событие", Toast.LENGTH_SHORT).show()
                setupRegisterButton(eventTitle)
            }
        }
    }

    private fun unregisterFromEvent(eventTitle: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            val userDao = EventAppDB.getDB(this@AreaDetailsActivity).userDao()
            val userId = getUserId(this@AreaDetailsActivity)

            val user = userDao.getUserById(userId)
            val updatedAreas = user?.areas?.let {
                val areas = kotlinx.serialization.json.Json.decodeFromString<MutableList<String>>(it)
                areas.remove(eventTitle)
                kotlinx.serialization.json.Json.encodeToString(areas)
            } ?: ""

            userDao.updateUserAreas(userId, updatedAreas)

            withContext(Dispatchers.Main) {
                Toast.makeText(this@AreaDetailsActivity, "Вы убрали запись на событие", Toast.LENGTH_SHORT).show()
                setupRegisterButton(eventTitle)
            }
        }
    }

    private fun loadAreaDetails(areaName: String) {
        db = EventAppDB.getDB(this)
        val eventsDao = db.eventLocationDao()

        lifecycleScope.launch {
            try {
                val existingEvent = eventsDao.getAreaByName(areaName)
                if (existingEvent != null) {
                    areaTitle.text = existingEvent?.eventTitle
                    areaDate.text = existingEvent?.eventDate
                    areaPlace.text = existingEvent?.address
                    areaInfo.text = existingEvent?.eventInfo

                    setupRegisterButton(areaTitle.text.toString())
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