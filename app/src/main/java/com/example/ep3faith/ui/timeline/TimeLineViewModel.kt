package com.example.ep3faith.ui.timeline

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
import com.example.ep3faith.database.post.PostWithReactions
import com.example.ep3faith.database.reaction.DatabaseReaction
import com.example.ep3faith.database.user.DatabaseUser
import com.example.ep3faith.database.user.UserFavoritePostsCrossRef
import kotlinx.coroutines.*
import timber.log.Timber

class TimeLineViewModel(val database: FaithDatabaseDAO, application: Application): AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val users: List<DatabaseUser> = listOf(DatabaseUser("quinten.dobbelaere@gmail.com", "Nicerdicer", "content://com.android.providers.media.documents/document/image%3A107958"),DatabaseUser("quinten.dobbelaere@student.hogent.be", "tryhard", "content://com.android.providers.media.documents/document/image%3A107958"))
    /*private val initPosts: List<Post> = listOf(
        Post(0,"NicerDicer","Grandma decorating the tree","","https://PayForMyGrandmaTree"),
        Post(0,"PoopyButtHole","Grandma decorating this mf","","https://PayForMyGrandmaDEN"),
        Post(0,"PoopyButtHole","Grandma decorating this mf","","https://PayForMyGrandmaDEN"),
        Post(0,"PoopyButtHole","Grandma decorating this mf","","https://PayForMyGrandmaDEN"),
        Post(0,"PoopyButtHole","Grandma decorating this mf","","https://PayForMyGrandmaDEN"),
    )*/
    private var _user = MutableLiveData<DatabaseUser>()
    val user: LiveData<DatabaseUser>
        get() = _user

    private var _posts = MutableLiveData<List<PostWithReactions>>()
    val posts: LiveData<List<PostWithReactions>>
        get() = _posts



    init {
        Timber.i("Initialized")
        getCredentials()
        initDB()
        gatherPosts()
    }


    //INITIALIZE THE DB

    private fun initDB() {
        uiScope.launch {
            dbClear()
            dBinit()
        }
    }

    private suspend fun dBinit(){
        withContext(Dispatchers.IO) {
           database.insertUsers(users)
            //database.insertPosts(initPosts)
        }
    }

    //CLEAR THE DB

    private suspend fun dbClear() {
        withContext(Dispatchers.IO) {
            database.clearUsers()
            //database.clearPosts()
        }
    }
    override fun onCleared() {
        super.onCleared()
        Timber.i("Cleared")
    }

    //GET THE POSTS FROM THE DB

    fun gatherPosts(){
        uiScope.launch {
            Timber.i("Gathering Posts")
            _posts.value = getPosts()
        }
    }

    private suspend fun getPosts(): List<PostWithReactions>?{
        var posts: List<PostWithReactions>?
        withContext(Dispatchers.IO){
            posts = database.getPostWithReactions()
        }
        return posts
    }

    // ADDING AND RETRIEVING FAVORITES TO DB

    fun addFavorite(postId: Int) {
        Timber.i("adding a favorite")
        val favorite = user.value?.let { UserFavoritePostsCrossRef(it.email, postId) }
        uiScope.launch {
            if (favorite != null) {
                favoriteToDb(favorite)
            }
        }
    }

    private suspend fun favoriteToDb(favorite: UserFavoritePostsCrossRef){
        withContext(Dispatchers.IO){
            database.insertFavorite(favorite)
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
        gatherPosts()
    }

    private suspend fun insertReactionDb(reaction: DatabaseReaction){
        withContext(Dispatchers.IO){
            database.insertReaction(reaction)
        }
    }

    // DELETE A POST

    fun deletePost(postId: Int) {
        uiScope.launch {
            deletePostDb(postId)
        }
    }

    private suspend fun deletePostDb(postId: Int){
        withContext(Dispatchers.IO){
            val post = database.getPostById(postId)
            database.deletePost(post)
            gatherPosts()
        }
    }

    // DELETE REACTION

    fun deleteReaction(reactionId: Int) {
        uiScope.launch {
            deleteReactionByIdDb(reactionId)
        }
    }

    private suspend fun deleteReactionByIdDb(reactionId: Int){
        withContext(Dispatchers.IO){
            val reaction = database.getReactionById(reactionId)
            database.deleteReaction(reaction)
            gatherPosts()
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