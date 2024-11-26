package com.example.eventapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameTxt) // ID поля логина
        passwordEditText = findViewById(R.id.passwordTxt) // ID поля пароля
        loginButton = findViewById(R.id.loginBtn)         // ID кнопки входа

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // Проверяем, что поля не пустые
            if (username.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Если не пустые, проверяем данные в базе данных
            lifecycleScope.launch(Dispatchers.IO) {
                val user = checkUserInDatabase(username, password)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        Toast.makeText(this@LoginActivity, "Успешный вход!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@LoginActivity, "Неверные логин или пароль", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    // Функция для проверки пользователя в базе данных
    private suspend fun checkUserInDatabase(username: String, password: String): User? {
        val database = AppDatabase.getInstance(this)
        val userDao = database.userDao()

        // Получаем пользователя с указанным логином
        val user = userDao.getUserByLogin(username)

        // Проверяем, что пароль совпадает
        return if (user != null && user.password == password) user else null
    }
}

