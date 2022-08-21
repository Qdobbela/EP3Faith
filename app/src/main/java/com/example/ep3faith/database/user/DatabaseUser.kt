package com.example.ep3faith.database.user

import androidx.room.*
import com.example.ep3faith.database.post.DatabasePost
import com.example.ep3faith.domain.Post
import com.example.ep3faith.domain.User


@Entity(tableName = "user_table")
class DatabaseUser constructor(

    @PrimaryKey
    var email: String,

    var username: String,

    var profilePicture: String,

    )

@Entity(primaryKeys = ["email", "postId"])
data class UserFavoritePostsCrossRef(
    val email: String,

    val postId: Int

)

data class UserWithPosts(
    @Embedded val user: DatabaseUser,
    @Relation(
        parentColumn = "email",
        entityColumn = "postId",
        associateBy = Junction(UserFavoritePostsCrossRef::class)
    )
    val post: List<DatabasePost>
)

fun List<DatabaseUser>.asDomainModel(): List<User> {
    return map {
        User (
            it.email,
            it.username,
            it.profilePicture
                )
    }
}