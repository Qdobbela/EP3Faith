package com.example.ep3faith.ui.addPost

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.auth0.android.Auth0
import com.auth0.android.authentication.AuthenticationAPIClient
import com.auth0.android.authentication.AuthenticationException
import com.auth0.android.authentication.storage.CredentialsManager
import com.auth0.android.authentication.storage.CredentialsManagerException
import com.auth0.android.authentication.storage.SharedPreferencesStorage
import com.auth0.android.callback.Callback
import com.auth0.android.result.Credentials
import com.auth0.android.result.UserProfile
import com.example.ep3faith.database.FaithDatabaseDAO
import com.example.ep3faith.database.Post
import com.example.ep3faith.database.User
import kotlinx.coroutines.*
import timber.log.Timber


class EditPostViewModel(val database: FaithDatabaseDAO, application: Application): AndroidViewModel(application) {
    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private lateinit var user : User

    private var _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean>
        get() = _saved

    private var _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    init {
        getCredentials()
    }

    //SAVING A POST TO THE DB

    fun editPost(postId: Int, caption: String, link: String, imageUri: Uri) {
        val post = Post( postId, user.username, user.email , caption, imageUri.toString(),link)
        uiScope.launch {
            _saved.value = dbPostOpslaan(post)
        }
    }

    private suspend fun dbPostOpslaan(post: Post): Boolean{
        withContext(Dispatchers.IO) {
            database.updatePost(post)
        }
        return true
    }

    // GET POST BY ID

    fun getPost(postId: Int) {
        uiScope.launch {
            getPostDb(postId)
        }
    }

    private suspend fun getPostDb(postId: Int) {
        withContext(Dispatchers.IO){
            _post.postValue(database.getPostById(postId))
        }
    }

    /*
    THIS PART IS FOR ACQUIRING THE USER'S CREDENTIALS
     */


    private fun getCredentials(){
        val account = Auth0(
            "4AwgfcJ6inGtdUDuVWv3jX9Nwmp6FcDG",
            "dev-4i-4zxou.us.auth0.com"
        )

        val apiClient = AuthenticationAPIClient(account)
        val manager = CredentialsManager(apiClient, SharedPreferencesStorage(getApplication()))
        var credentials: Credentials

        manager.getCredentials(object: Callback<Credentials, CredentialsManagerException> {
            override fun onSuccess(cred: Credentials) {
                Timber.i("Credentials acquired")
                credentials = cred
                showUserProfile(account, credentials.accessToken)
            }

            override fun onFailure(error: CredentialsManagerException) {
                // No credentials were previously saved or they couldn't be refreshed
                Timber.i("getting credentials failed: %s", error.message)
            }
        })


    }

    private fun showUserProfile(account: Auth0, accessToken: String) {
        val client = AuthenticationAPIClient(account)

        // With the access token, call `userInfo` and get the profile from Auth0.
        client.userInfo(accessToken)
            .start(object : Callback<UserProfile, AuthenticationException> {
                override fun onFailure(exception: AuthenticationException) {
                    // Something went wrong!
                }

                override fun onSuccess(profile: UserProfile) {
                    // We have the user's profile!
                    profile.email?.let { getUser(it) }
                }
            })
    }

    private fun getUser(email: String){
        uiScope.launch {
            val theUser = getUserFromDB(email)
            user = theUser
        }
    }

    private suspend fun getUserFromDB(email: String): User {
        val userByMail: User
        withContext(Dispatchers.IO) {
            userByMail = database.getUserByEmail(email)
        }
        return userByMail
    }

}