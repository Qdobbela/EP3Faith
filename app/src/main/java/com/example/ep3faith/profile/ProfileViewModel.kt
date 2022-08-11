package com.example.ep3faith.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ep3faith.database.FaithDatabaseDAO
import com.example.ep3faith.database.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber

class ProfileViewModel(val database: FaithDatabaseDAO, application: Application)
        : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private lateinit var user: User

    private val _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username

    init {
        Timber.i("Initialized")
        _username.value = "JohnDoe"
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("Cleared")
    }

    fun changeUsername(newName: String){
        _username.value = newName
    }
}