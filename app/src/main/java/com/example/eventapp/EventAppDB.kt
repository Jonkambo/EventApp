package com.example.eventapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.eventapp.Data.Comment
import com.example.eventapp.Data.EventLocation
import com.example.eventapp.Data.Role
import com.example.eventapp.Data.User

@Database(entities = [Role::class, User::class, Comment::class, EventLocation::class], version = 1)
abstract class EventAppDB : RoomDatabase() {

    abstract fun roleDao(): RoleDao
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao
    abstract fun eventLocationDao(): EventLocationDao

    companion object {
        fun getDB(context: Context): EventAppDB {
            return Room.databaseBuilder(
                context.applicationContext,
                EventAppDB::class.java,
                "EventApp.db"
            ).build()
        }
    }
}