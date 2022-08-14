package com.example.ep3faith.timeline

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
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

        getCredentials()
    }

    private fun initDB() {
        uiScope.launch {
            dbClear()
            dBinit()
        }
    }

    private suspend fun dbClear() {
        withContext(Dispatchers.IO) {
            database.clear()
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

    private fun getCredentials(){
        var account = Auth0(
            "4AwgfcJ6inGtdUDuVWv3jX9Nwmp6FcDG",
            "dev-4i-4zxou.us.auth0.com"
        )

        val apiClient = AuthenticationAPIClient(account)
        val manager = CredentialsManager(apiClient, SharedPreferencesStorage(getApplication()))

        manager.getCredentials(object: Callback<Credentials, CredentialsManagerException> {
            override fun onSuccess(credentials: Credentials) {
                Timber.i("Accestoken: %s", credentials.accessToken)
            }

            override fun onFailure(error: CredentialsManagerException) {
                // No credentials were previously saved or they couldn't be refreshed
                Timber.i("getting credentials failed: %s", error.message)
            }
        })
    }
}