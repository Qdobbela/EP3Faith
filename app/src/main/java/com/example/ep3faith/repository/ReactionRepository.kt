package com.example.ep3faith.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.database.reaction.asDomainModel
import com.example.ep3faith.domain.Reaction

class ReactionRepository(private val database: FaithDatabase) {
    val reactions = MediatorLiveData<LiveData<Reaction>>()

    private var changeableLiveDataPost = Transformations.map(database.faithDatabaseDAO.getReactions()) {
        it.asDomainModel()
    }
}
