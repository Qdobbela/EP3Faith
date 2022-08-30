package com.example.ep3faith.ui.favorites

import android.app.Application
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
import com.example.ep3faith.database.*
import com.example.ep3faith.database.post.DatabasePost
import com.example.ep3faith.database.post.PostWithReactions
import com.example.ep3faith.database.user.DatabaseUser
import com.example.ep3faith.database.user.UserFavoritePostsCrossRef
import kotlinx.coroutines.*
import timber.log.Timber

class FavoritesViewModel(val database: FaithDatabaseDAO, application: Application) : AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _user = MutableLiveData<DatabaseUser>()
    val user: LiveData<DatabaseUser>
        get() = _user

    private var _favorites = MutableLiveData<List<PostWithReactions>>()
    val favorites: LiveData<List<PostWithReactions>>
        get() = _favorites

    init {
        getCredentials()
    }

    fun gatherFavorites() {
        uiScope.launch {
            gatherFavoritesFromDb()
        }
    }

    private suspend fun gatherFavoritesFromDb() {
        val posts: List<DatabasePost>
        val postIdList: MutableList<Int> = mutableListOf()
        var postReactionList: List<PostWithReactions>
        withContext(Dispatchers.IO) {
            posts = user.value?.let { database.getUserWithFavorites(it.email).post }!!
            Timber.i("got posts from DB: %s", posts)
            for (post in posts) {
                postIdList.add(post.postId)
            }
            postReactionList = database.getFavoritesWithReactions(postIdList)
        }
        Timber.i("favorites gathered: %s", postReactionList)
        _favorites.postValue(postReactionList)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("Cleared")
    }

    fun removeFavorite(postId: Int) {
        uiScope.launch {
            val favorite = user.value?.let { UserFavoritePostsCrossRef(it.email, postId) }
            if (favorite != null) {
                Timber.i("Favorite isn't null")
                removeFavoriteDb(favorite)
            } else {
                Timber.i("Oh no Favorite is null...")
            }
        }
    }

    private suspend fun removeFavoriteDb(favorite: UserFavoritePostsCrossRef) {
        withContext(Dispatchers.IO) {
            database.deleteFavorite(favorite)
        }
    }

    /*
    THIS PART IS FOR ACQUIRING THE USER'S CREDENTIALS
     */

    private fun getCredentials() {
        val account = Auth0(
            "4AwgfcJ6inGtdUDuVWv3jX9Nwmp6FcDG",
            "dev-4i-4zxou.us.auth0.com"
        )

        val apiClient = AuthenticationAPIClient(account)
        val manager = CredentialsManager(apiClient, SharedPreferencesStorage(getApplication()))
        var credentials: Credentials

        manager.getCredentials(object : Callback<Credentials, CredentialsManagerException> {
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

    private fun getUser(email: String) {
        uiScope.launch {
            val theUser = getUserFromDB(email)
            _user.postValue(theUser)
            gatherFavorites()
        }
    }

    private suspend fun getUserFromDB(email: String): DatabaseUser {
        val userByMail: DatabaseUser
        withContext(Dispatchers.IO) {
            userByMail = database.getUserByEmail(email)
        }
        return userByMail
    }
}
