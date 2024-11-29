package com.example.eventapp

import android.content.Context
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoles(vararg roles: Role)

    @Query("SELECT * FROM Roles")
    suspend fun getAllRoles(): List<Role>
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("UPDATE Users SET UserInfo = :userInfo WHERE UserID = :userId")
    suspend fun updateUserInfo(userId: Int, userInfo: String)

    @Query("SELECT * FROM Users")
    fun getAllUsers(): Flow<List<User>>

    @Query("SELECT * FROM Users WHERE login = :login LIMIT 1")
    suspend fun getUserByLogin(login: String): User?

    @Query("SELECT * FROM Users WHERE UserID = :userID LIMIT 1")
    fun getUserById(userID: Int): User?

    @Query("SELECT login FROM Users WHERE UserID = :userID LIMIT 1")
    fun getLoginById(userID: Int): String?

    @Query("SELECT * FROM Users WHERE login = :login AND password = :password LIMIT 1")
    suspend fun checkUserInDatabase(login: String, password: String): User?

    @Query("UPDATE Users SET UserInfo = :userInfo, ProfilePhoto = :profilePhoto WHERE UserID = :userId")
    suspend fun updateUserProfile(userId: Int, userInfo: String, profilePhoto: ByteArray?)
}

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: Comment)

    @Query("SELECT * FROM Comments")
    fun getAllComments(): Flow<List<Comment>>
}

@Dao
interface EventLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventLocation(eventLocation: EventLocation)

    @Query("SELECT * FROM EventLocations")
    fun getAllEventLocations(): Flow<List<EventLocation>>

    @Query("SELECT * FROM EventLocations WHERE eventTitle = :areaName LIMIT 1")
    suspend fun getAreaByName(areaName: String): EventLocation?
}
