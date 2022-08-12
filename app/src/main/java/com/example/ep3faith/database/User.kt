package com.example.ep3faith.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
class User(

    @PrimaryKey
    var email: String,

    var username: String,

    var profilePicture: String

    )