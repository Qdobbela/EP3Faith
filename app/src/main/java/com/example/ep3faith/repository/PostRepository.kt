package com.example.ep3faith.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Transformations
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.database.post.asDomainModel
import com.example.ep3faith.domain.Post

class PostRepository(private val database: FaithDatabase) {
    val posts = MediatorLiveData<LiveData<Post>>()

    private var changeableLiveDataPost = Transformations.map(database.faithDatabaseDAO.getPosts()) {
        it.asDomainModel()
    }
}
