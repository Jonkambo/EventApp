package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
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

class SignUpActivity : AppCompatActivity() {
    private var signinButton: Button? = null
    private var signupButton: Button? = null
    private lateinit var login: TextView
    private lateinit var password: TextView
    private lateinit var repeat_password: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_up)

        login = findViewById(R.id.usernameTxt)
        password = findViewById(R.id.passwordTxt)
        repeat_password = findViewById(R.id.repeatPasswordTxt)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        signinButton = findViewById(R.id.becomeBtn)
        signinButton?.setOnClickListener {
            if (login.text.isEmpty() || password.text.isEmpty() || repeat_password.text.isEmpty() || password.text.toString() != repeat_password.text.toString()) {
                if(login.text.isEmpty()){
                    Toast.makeText(this, "Введите логин", Toast.LENGTH_LONG).show()
                }
                else if (password.text.isEmpty() || repeat_password.text.isEmpty() || password.text.toString() != repeat_password.text.toString()) {
                    if (password.text.isEmpty() || repeat_password.text.isEmpty()) {
                        Toast.makeText(this, "Введите пароль", Toast.LENGTH_LONG).show()
                    }
                    else if(password.text.toString() != repeat_password.text.toString()){
                        Toast.makeText(this, "Пароль не совпадает", Toast.LENGTH_LONG).show()
                    }
                }
            }
            else {
                lifecycleScope.launch {
                    val db = EventAppDB.getDB(this@SignUpActivity)
                    val userDaoo = db.userDao()
                    val existingUser  = userDaoo.getUserByLogin(login.text.toString())
                    if (existingUser  == null) {
                        val newUser  = User(login = login.text.toString(), password = password.text.toString())
                        userDaoo.insertUser(newUser)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@SignUpActivity, "Аккаунт создан", Toast.LENGTH_SHORT).show()
                        }
                        val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        withContext(Dispatchers.Main) {
                        Toast.makeText(this@SignUpActivity, "Такой логин уже существует", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }

        signupButton = findViewById(R.id.retrnToLoginBtn)
        signupButton?.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}