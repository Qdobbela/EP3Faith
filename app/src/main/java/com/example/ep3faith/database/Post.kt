package com.example.ep3faith.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_table")
class Post (

    @PrimaryKey
    var postId: String,

    var username: String,

    var caption: String,

    var picture: String,

    var link: String
)