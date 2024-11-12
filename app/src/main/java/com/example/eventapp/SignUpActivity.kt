package com.example.eventapp

import android.content.Context
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
import androidx.room.Room
import com.example.eventapp.Data.AppDatabase
import com.example.eventapp.Data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignUpActivity : AppCompatActivity() {
    var signinButton: Button? = null
    var signupButton: Button? = null
    val login:TextView = findViewById(R.id.usernameTxt)
    val loginStr = login.text.toString()
    var password:TextView = findViewById(R.id.passwordTxt)
    var repeat_password:TextView = findViewById(R.id.repeatPasswordTxt)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()
        setContentView(R.layout.activity_sign_up)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        signinButton = findViewById(R.id.becomeBtn)

        signinButton?.setOnClickListener {
            val intent = Intent(this@SignUpActivity,LoginActivity::class.java)
            startActivity(intent)
        }
        signupButton = findViewById(R.id.retrnToLoginBtn)

        signupButton?.setOnClickListener {
            if(login.text.isEmpty()){
                Toast.makeText(this, "Введите логин", Toast.LENGTH_LONG).show()
            }
            else if ((password.text.isEmpty() or repeat_password.text.isEmpty()) && password.text!=repeat_password){
                if (password.text.isEmpty() or repeat_password.text.isEmpty()){
                    Toast.makeText(this, "Введите пароль", Toast.LENGTH_LONG).show()
                }
                else{
                    lifecycleScope.launch {
                        val db = Room.databaseBuilder(this@SignUpActivity, AppDatabase::class.java, "app_database").build()
                        val userDaoo = db.userDao()
                        val existingUser = userDaoo.getUserByLogin(login.text.toString())
                        if (existingUser == null) {
                            val newUser = User(login = login.text.toString(), password = password.text.toString(), roleId = 0)//посмотреть что сделать с айди
                            userDaoo.insertUser(newUser)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@SignUpActivity, "Аккаунт создан, перейдите на страницу авторизации", Toast.LENGTH_SHORT).show()
                            }
                        }else {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(this@SignUpActivity, "Такой логин уже существует", Toast.LENGTH_SHORT).show()
                            }
                        }
                    val intent = Intent(this@SignUpActivity,LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}