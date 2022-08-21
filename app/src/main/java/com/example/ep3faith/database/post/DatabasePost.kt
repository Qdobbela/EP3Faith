package com.example.ep3faith.database.post

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.ep3faith.database.reaction.DatabaseReaction
import com.example.ep3faith.domain.Post

@Entity(tableName = "post_table",indices = [Index(value = ["emailUser"])])
class DatabasePost constructor(

    @PrimaryKey(autoGenerate = true)
    var postId: Int,

    var username: String,

    var emailUser: String,

    var caption: String,

    var picture: String,

    var link: String
)

data class PostWithReactions(
    @Embedded val post: Post,
    @Relation(
        parentColumn = "postId",
        entityColumn = "hostPostId"
    )
    val reactions: List<DatabaseReaction>
)

fun List<DatabasePost>.asDomainModel(): List<Post> {
    return map {
        Post (
            postId = it.postId,
            username = it.username,
            emailUser = it.emailUser,
            caption = it.picture,
            link = it.link)
    }
}