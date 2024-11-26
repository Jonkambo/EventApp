package com.example.eventapp

import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eventapp.Data.saveUserId
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var toSignUoBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar()?.hide();
        setContentView(R.layout.activity_login)

        usernameEditText = findViewById(R.id.usernameTxt) // ID поля логина
        passwordEditText = findViewById(R.id.passwordTxt) // ID поля пароля
        loginButton = findViewById(R.id.loginBtn)         // ID кнопки входа
        toSignUoBtn = findViewById(R.id.returnSignInBtn)

        toSignUoBtn?.setOnClickListener{
            val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

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
                val db = EventAppDB.getDB(this@LoginActivity)
                val userDaoo = db.userDao()
                val user = userDaoo.checkUserInDatabase(username, password)
                withContext(Dispatchers.Main) {
                    if (user != null) {
                        Toast.makeText(this@LoginActivity, "Успешный вход!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@LoginActivity,BasicPageActivity::class.java);
                        saveUserId(this@LoginActivity, user.userId)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this@LoginActivity, "Неверные логин или пароль", Toast.LENGTH_SHORT).show()
                        return@withContext
                    }
                }
            }
        }
    }

    // Функция для проверки пользователя в базе данных
    private suspend fun checkUserInDatabase(username: String, password: String): User? {
        val database = EventAppDB.getDB(this)
        val userDao = database.userDao()

        // Получаем пользователя с указанным логином
        val user = userDao.getUserByLogin(username)

        // Проверяем, что пароль совпадает
        return if (user != null && user.password == password) user else null
    }
}

