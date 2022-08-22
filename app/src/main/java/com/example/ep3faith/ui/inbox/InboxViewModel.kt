package com.example.ep3faith.ui.inbox

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
import com.example.ep3faith.database.reaction.DatabaseReaction
import com.example.ep3faith.database.user.DatabaseUser
import com.example.ep3faith.database.user.UserInboxPostsCrossRef
import kotlinx.coroutines.*
import timber.log.Timber

class InboxViewModel(val database: FaithDatabaseDAO, application: Application): AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _user = MutableLiveData<DatabaseUser>()
    val user: LiveData<DatabaseUser>
        get() = _user

    private var _Inbox = MutableLiveData<List<PostWithReactions>>()
    val Inbox: LiveData<List<PostWithReactions>>
        get() = _Inbox

    init {
        getCredentials()
    }

    fun gatherInbox() {
        uiScope.launch {
            gatherInboxFromDb()
        }
    }

    private suspend fun gatherInboxFromDb() {
        val posts: List<DatabasePost>
        val postIdList: MutableList<Int> = mutableListOf()
        var postReactionList: List<PostWithReactions>
        withContext(Dispatchers.IO){
            posts = user.value?.let { database.getUserWithInbox(it.email).post }!!
            for(post in posts){
                postIdList.add(post.postId)
            }
            postReactionList = database.getFavoritesWithReactions(postIdList)
        }
        _Inbox.postValue(postReactionList)
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("Cleared")
    }

    fun removeInbox(postId: Int) {
        uiScope.launch {
            val inbox = user.value?.let{ UserInboxPostsCrossRef(it.email,postId) }
            if (inbox != null) {
                removeInboxDb(inbox)
            }
            gatherInbox()
        }
    }

    private suspend fun removeInboxDb(Inbox: UserInboxPostsCrossRef) {
        withContext(Dispatchers.IO){
            database.deleteInbox(Inbox)
        }
    }

    // ADDING REACTIONS

    fun addReaction(reactionText: String, postId: Int) {
        Timber.i("reaction text: %s", reactionText)
        val reaction = user.value?.let { DatabaseReaction(0, reactionText, it.username, postId, it.email) }
        uiScope.launch {
            if (reaction != null) {
                insertReactionDb(reaction)
            }
        }
        removeInbox(postId)
        gatherInbox()
    }

    private suspend fun insertReactionDb(reaction: DatabaseReaction){
        withContext(Dispatchers.IO){
            database.insertReaction(reaction)
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
            _user.postValue(theUser)
            gatherInbox()
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