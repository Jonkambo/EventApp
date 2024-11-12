package com.example.eventapp.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Role::class, User::class, Comment::class, EventLocation::class], version = 1)
abstract class EventAppDB : RoomDatabase() {

    abstract fun roleDao(): RoleDao
    abstract fun userDao(): UserDao
    abstract fun commentDao(): CommentDao
    abstract fun eventLocationDao(): EventLocationDao

    companion object {
        @Volatile
        private var INSTANCE: EventAppDB? = null
        fun getDB(context: Context): EventAppDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                context.applicationContext,
                EventAppDB::class.java,
                "EventApp.db"
            ).build()
            INSTANCE = instance
            instance
            }
        }
    }
}