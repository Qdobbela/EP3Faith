package com.example.ep3faith.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ep3faith.database.FaithDatabaseDAO
import com.example.ep3faith.database.User
import kotlinx.coroutines.*
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
        getUser("quinten.dobbelaere@gmail.com")
    }

    private fun getUser(email: String){
        uiScope.launch {
            val theUser = getUserFromDB(email)
            user = theUser
            _username.value = user.username
        }
    }

    private suspend fun getUserFromDB(email: String): User{
        val userByMail: User
        withContext(Dispatchers.IO) {
            userByMail = database.getUserByEmail(email)
        }
        return userByMail
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("Cleared")
    }

    fun changeUsername(newName: String){
        _username.value = newName
        user.username = newName
    }

}