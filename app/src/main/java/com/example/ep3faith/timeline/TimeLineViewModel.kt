package com.example.ep3faith.timeline

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.ep3faith.database.FaithDatabaseDAO
import com.example.ep3faith.database.User
import kotlinx.coroutines.*
import timber.log.Timber

class TimeLineViewModel(val database: FaithDatabaseDAO, application: Application): AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var users: List<User> = listOf(User("quinten.dobbelaere@gmail.com", "Nicerdicer", ""),User("quinten.dobbelaere@student.hogent.be", "tryhard", ""))

    init {
        Timber.i("Initialized")
        initDB()
    }

    private fun initDB() {
        uiScope.launch {
            dBinit()
        }
    }

    private suspend fun dBinit(){
        withContext(Dispatchers.IO) {
            database.insertUsers(users)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("Cleared")
    }
}