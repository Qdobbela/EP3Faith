package com.example.ep3faith.domain

import androidx.room.Entity
import androidx.room.PrimaryKey


data class Post (

    var postId: Int = 0,

    var username: String = "",

    var emailUser: String = "",

    var caption: String = "",

    var picture: String = "",

    var link: String = ""
)

data class User(

    var email: String = "",

    var username: String = "",

    var profilePicture: String = "",

    )

data class Reaction(

    var reactionId: Int = 0,

    var reactionText: String = "",

    var reactionUser: String = "",

    var hostPostId: Int = 0,

    var reactionUserEmail: String = ""
)

