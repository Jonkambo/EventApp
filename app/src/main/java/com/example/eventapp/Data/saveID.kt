package com.example.eventapp.Data

import android.content.Context


fun saveUserId(context: Context, userId: Int) {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()
    editor.putInt("USER_ID", userId)
    editor.apply()
}

fun getUserId(context: Context): Int {
    val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
    return sharedPreferences.getInt("USER_ID", -1) // null, если ID не сохранен
}