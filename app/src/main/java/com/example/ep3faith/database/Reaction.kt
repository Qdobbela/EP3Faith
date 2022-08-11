package com.example.ep3faith.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reaction_table")
class Reaction {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    var tekst: String = ""

    var user: String = ""
}