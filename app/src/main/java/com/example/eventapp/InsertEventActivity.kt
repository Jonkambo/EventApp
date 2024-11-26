package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class InsertEventActivity : AppCompatActivity() {

    private lateinit var db: EventAppDB
    private lateinit var eventTitleEditText: EditText
    private lateinit var eventDateEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var eventInfoEditText: EditText
    private lateinit var saveButton: Button
    private lateinit var returntoHomeBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_insert_event)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        returntoHomeBtn = findViewById(R.id.returntoHomeBtn)
        returntoHomeBtn?.setOnClickListener {
            val intent = Intent(this@InsertEventActivity,BasicPageActivity::class.java);
            startActivity(intent)
        }

        db = EventAppDB.getDB(this)

        eventTitleEditText = findViewById(R.id.eventTitleEditText)
        eventDateEditText = findViewById(R.id.eventDateEditText)
        addressEditText = findViewById(R.id.addressEditText)
        eventInfoEditText = findViewById(R.id.eventInfoEditText)
        saveButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            saveEvent()
        }
    }

    // сохоаняем в базу данных мероприятие
    private fun saveEvent() {
        val title = eventTitleEditText.text.toString()
        val date = eventDateEditText.text.toString()
        val address = addressEditText.text.toString()
        val info = eventInfoEditText.text.toString()

        if (title.isBlank() || date.isBlank() || address.isBlank()) {
            Toast.makeText(this, "Пожалуйста, заполните все обязательные поля.", Toast.LENGTH_SHORT).show()
            return
        }

        val eventLocation = EventLocation(eventTitle = title, eventDate = date, address = address, eventInfo = info)

        lifecycleScope.launch {
            db.eventLocationDao().insertEventLocation(eventLocation)
            Toast.makeText(this@InsertEventActivity, "Мероприятие добавлено!", Toast.LENGTH_SHORT).show()

            finish() // Закрываем активность
        }
    }
}