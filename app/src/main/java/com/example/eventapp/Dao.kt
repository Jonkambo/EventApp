package com.example.eventapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.eventapp.Data.Comment
import com.example.eventapp.Data.EventLocation
import com.example.eventapp.Data.Role
import com.example.eventapp.Data.User
import kotlinx.coroutines.flow.Flow

@Dao
interface RoleDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRoles(vararg roles: Role)
}

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT * FROM Users")
    fun getAllUsers(): Flow<List<User>>
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
}