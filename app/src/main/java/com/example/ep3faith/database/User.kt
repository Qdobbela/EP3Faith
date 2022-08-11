package com.example.ep3faith.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "youngster_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L,

    var email: String = "",

    var username: String = "",

    val profilepicture: String = ""
)