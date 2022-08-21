package com.example.ep3faith.database.reaction

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation
import com.example.ep3faith.domain.Reaction
import com.example.ep3faith.domain.User

@Entity(tableName = "reaction_table")
data class DatabaseReaction constructor(

    @PrimaryKey(autoGenerate = true)
    var reactionId: Int,

    var reactionText: String,

    var reactionUser: String,

    var hostPostId: Int,

    var reactionUserEmail: String
)

data class ReactionAndUser(
    @Embedded val user: User,
    @Relation(
        parentColumn = "email",
        entityColumn = "reactionUser"
    )
    val reaction: DatabaseReaction
)

fun List<DatabaseReaction>.asDomainModel(): List<Reaction> {
    return map {
        Reaction (
            it.reactionId,
            it.reactionText,
            it.reactionUser,
            it.hostPostId,
            it.reactionUserEmail
                )
    }
}