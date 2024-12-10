package com.example.eventapp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Role::class, User::class, Comment::class, EventLocation::class], version = 2, exportSchema = false)
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
                ).addCallback(object : Callback() {
                    override fun onOpen(db: SupportSQLiteDatabase) {
                        super.onOpen(db)
                        CoroutineScope(Dispatchers.IO).launch {
                            INSTANCE?.let { database ->
                                val existingRoles = database.roleDao().getAllRoles()
                                val existAdmin = database.userDao().getUserByLogin("admin")
                                if (existingRoles.isEmpty()) {
                                    val roles = listOf(
                                        Role(roleId = 1, roleName = "user"),
                                        Role(roleId = 2, roleName = "admin")
                                    )
                                    database.roleDao().insertRoles(*roles.toTypedArray())
                                }
                                
                                if (existAdmin == null) {
                                    database.userDao().insertUser(User(login = "admin", password = "123", roleId = 2))
                                }
                            }
                        }
                    }
                })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}