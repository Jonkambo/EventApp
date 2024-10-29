package com.example.eventapp

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.Date

@Entity(
    tableName = "Roles",
    indices = [Index(value = ["RoleName"], unique = true)]
)
data class Role (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "RoleID")
    val roleId: Int = 0,

    @ColumnInfo(name = "RoleName")
    val roleName: String
)

@Entity(
    tableName = "Users",
    indices = [Index(value = ["Login"], unique = true)],
    foreignKeys = [
        ForeignKey(
            entity = Role::class,
            parentColumns = ["RoleID"],
            childColumns = ["RoleID"],
            onDelete = ForeignKey.RESTRICT,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class User(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "userID")
    val userId: Int = 0,

    @ColumnInfo(name = "FullName")
    val fullName: String,

    @ColumnInfo(name = "Login")
    val login: String,

    @ColumnInfo(name = "Password")
    val password: String,

    @ColumnInfo(name = "RoleID")
    val roleId: Int
)

@Entity(
    tableName = "Comments",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["UserID"],
            childColumns = ["UserID"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class Comment(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "CommentID")
    val commentId: Int = 0,

    @ColumnInfo(name = "UserID")
    val userId: Int,

    @ColumnInfo(name = "Rating")
    val rating: Int,

    @ColumnInfo(name = "CommentText")
    val commentText: String,

    @ColumnInfo(name = "CommentDate")
    val commentDate: Date = Date()
)

@Entity(
    tableName = "EventLocations",
    indices = [Index(value = ["Address"])]
)
data class EventLocation(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "EventID")
    val eventId: Int = 0,

    @ColumnInfo(name = "EventDate")
    val eventDate: Date,

    @ColumnInfo(name = "Address")
    val address: String,

    @ColumnInfo(name = "EventInfo")
    val eventInfo: String?
)