package com.example.ep3faith.profile

import android.app.Application
import android.content.Intent
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.callback.Callback
import com.auth0.android.provider.WebAuthProvider
import com.example.ep3faith.MainActivity
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

    private val account = Auth0(
    "4AwgfcJ6inGtdUDuVWv3jX9Nwmp6FcDG",
    "dev-4i-4zxou.us.auth0.com"
    )

    private lateinit var user: User

    private val _username = MutableLiveData<String>()
    val username: LiveData<String>
        get() = _username


    private val _logout = MutableLiveData<Boolean>()
    val logout: LiveData<Boolean>
        get() = _logout

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