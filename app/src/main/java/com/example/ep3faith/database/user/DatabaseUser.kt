package com.example.ep3faith.database.user

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.ep3faith.database.post.DatabasePost
import com.example.ep3faith.domain.User

@Entity(tableName = "user_table")
class DatabaseUser constructor(

    @PrimaryKey
    var email: String,

    var username: String,

    var profilePicture: String,

    var counselor: Boolean

)

@Entity(primaryKeys = ["email", "postId"])
data class UserFavoritePostsCrossRef(
    val email: String,

    val postId: Int

)

@Entity(primaryKeys = ["email", "postId"])
data class UserInboxPostsCrossRef(
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

data class UserWithInbox(
    @Embedded val user: DatabaseUser,
    @Relation(
        parentColumn = "email",
        entityColumn = "postId",
        associateBy = Junction(UserInboxPostsCrossRef::class)
    )
    val post: List<DatabasePost>
)

fun List<DatabaseUser>.asDomainModel(): List<User> {
    return map {
        User(
            it.email,
            it.username,
            it.profilePicture
        )
    }
}
