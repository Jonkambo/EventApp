package com.example.eventapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {
    var signinButton: Button? = null;
    var signupButton: Button? = null;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        signinButton = findViewById(R.id.loginBtn)

        signinButton?.setOnClickListener {
            val intent = Intent(this@MainActivity,BasicPageActivity::class.java);
            startActivity(intent);
        }
        signupButton = findViewById(R.id.returnSignInBtn)

        signupButton?.setOnClickListener {
            val intent = Intent(this@MainActivity,SignUpActivity::class.java);
            startActivity(intent);
        }
    }
}