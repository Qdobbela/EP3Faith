package com.example.ep3faith.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.database.user.asDomainModel
import com.example.ep3faith.domain.Reaction
import com.example.ep3faith.domain.User

class UserRepository(private val database: FaithDatabase) {
    val users = MediatorLiveData<LiveData<User>>()

    private var changeableLiveDataPost = Transformations.map(database.faithDatabaseDAO.getUsers()) {
        it.asDomainModel()
    }

}