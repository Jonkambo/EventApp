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


class MainActivity : AppCompatActivity() {
    var signinButton: Button? = null;
    var signupButton: Button? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        signinButton = findViewById(R.id.signInBtn)

        signinButton?.setOnClickListener {
            val intent = Intent(this@MainActivity,LoginActivity::class.java);
            startActivity(intent);
        }
        signupButton = findViewById(R.id.signUpBtn)

        signupButton?.setOnClickListener {
            val intent = Intent(this@MainActivity,SignUpActivity::class.java);
            startActivity(intent);
        }

    }
}
