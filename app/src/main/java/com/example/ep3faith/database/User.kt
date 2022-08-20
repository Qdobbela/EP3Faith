package com.example.ep3faith.database

import androidx.room.*

@Entity(tableName = "user_table")
class User(

    @PrimaryKey
    var email: String,

    var username: String,

    var profilePicture: String,

    )

@Entity(tableName = "post_table")
class Post (

    @PrimaryKey(autoGenerate = true)
    var postId: Int,

    var username: String,

    var caption: String,

    var picture: String,

    var link: String
)

@Entity(primaryKeys = ["email", "postId"])
data class UserFavoritePostsCrossRef(
    val email: String,

    val postId: Int

)

data class UserWithPosts(
    @Embedded val user: User,
    @Relation(
        parentColumn = "email",
        entityColumn = "postId",
        associateBy = Junction(UserFavoritePostsCrossRef::class)
    )
    val post: List<Post>
)


@Entity(tableName = "reaction_table")
data class Reaction(

    @PrimaryKey(autoGenerate = true)
    var reactionId: Int,

    var reactionText: String,

    var reactionUser: String,

    var hostPostId: Int
)

data class ReactionAndUser(
    @Embedded val user: User,
    @Relation(
        parentColumn = "email",
        entityColumn = "reactionUser"
    )
    val reaction: Reaction
)

data class PostWithReactions(
    @Embedded val post: Post,
    @Relation(
        parentColumn = "postId",
        entityColumn = "hostPostId"
    )
    val reactions: List<Reaction>
)