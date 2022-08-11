package com.example.ep3faith.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ep3faith.database.FaithDatabaseDAO
import timber.log.Timber

class ProfileViewModel(val faithDatabaseDAO: FaithDatabaseDAO, application: Application)
        : AndroidViewModel(application) {

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