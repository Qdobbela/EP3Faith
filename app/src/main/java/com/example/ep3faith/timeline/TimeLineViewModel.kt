package com.example.ep3faith.timeline

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
import kotlinx.coroutines.*
import timber.log.Timber

class TimeLineViewModel(val database: FaithDatabaseDAO, application: Application): AndroidViewModel(application) {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val users: List<User> = listOf(User("quinten.dobbelaere@gmail.com", "Nicerdicer", ""),User("quinten.dobbelaere@student.hogent.be", "tryhard", ""))
    /*private val initPosts: List<Post> = listOf(
        Post(0,"NicerDicer","Grandma decorating the tree","","https://PayForMyGrandmaTree"),
        Post(0,"PoopyButtHole","Grandma decorating this mf","","https://PayForMyGrandmaDEN"),
        Post(0,"PoopyButtHole","Grandma decorating this mf","","https://PayForMyGrandmaDEN"),
        Post(0,"PoopyButtHole","Grandma decorating this mf","","https://PayForMyGrandmaDEN"),
        Post(0,"PoopyButtHole","Grandma decorating this mf","","https://PayForMyGrandmaDEN"),
    )*/
    private lateinit var user: User

    //VARIABLE TO SEE IF POSTS HAS BEEN SAVED
    private var _saved = MutableLiveData<Boolean>()
    val saved: LiveData<Boolean>
        get() = _saved

    private var _posts = MutableLiveData<List<Post>>()
    val posts: LiveData<List<Post>>
        get() = _posts


    init {
        Timber.i("Initialized")
        initDB()
        getCredentials()
    }


    //INITIALIZE THE DB

    private fun initDB() {
        uiScope.launch {
            dbClear()
            dBinit()
            gatherPosts()
        }
    }

    private suspend fun dBinit(){
        withContext(Dispatchers.IO) {
           database.insertUsers(users)
            //database.insertPosts(initPosts)
        }
    }

    //GET THE POSTS FROM THE DB

    fun gatherPosts(){
        uiScope.launch {
            Timber.i("Gathering Posts")
            _posts.value = getPosts()
        }
    }

    private suspend fun getPosts(): List<Post>{
        var posts: List<Post>
        withContext(Dispatchers.IO){
            posts = database.getPosts()
        }
        return posts
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

    // ADDING AND RETRIEVING FAVORITES TO DB

    fun addFavorite(postId: Int) {
        Timber.i("adding a favorite")
        val favorite = UserFavoritePostsCrossRef(user.email, postId)
        uiScope.launch {
            favoriteToDb(favorite)
        }
    }

    private suspend fun favoriteToDb(favorite: UserFavoritePostsCrossRef){
        withContext(Dispatchers.IO){
            database.insertFavorite(favorite)
        }
    }


    private fun getFavorites(email: String) {
        uiScope.launch {
            getFavoritesDb(email)
        }
    }

    private suspend fun getFavoritesDb(email: String) {
        Timber.i("Getting Favorites")
        withContext(Dispatchers.IO){
            database.getUserWithFavorites(email)
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