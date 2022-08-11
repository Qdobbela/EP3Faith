package com.example.ep3faith.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "youngster_table")
data class Youngster(
    @PrimaryKey(autoGenerate = true)
    var youngsterId: Long = 0L,

    var username: String = "",

    val profilepicture: String = ""
)