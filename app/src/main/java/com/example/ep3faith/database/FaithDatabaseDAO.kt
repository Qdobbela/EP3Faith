package com.example.ep3faith.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.ep3faith.database.post.DatabasePost
import com.example.ep3faith.database.post.PostWithReactions
import com.example.ep3faith.database.reaction.DatabaseReaction
import com.example.ep3faith.database.user.*

@Dao
interface FaithDatabaseDAO {

    //USER

    @Query("SELECT * FROM user_table WHERE email = :email")
    fun getUserByEmail(email: String): DatabaseUser

    @Insert
    fun insertUsers(users: List<DatabaseUser>)

    @Query("DELETE FROM user_table")
    fun clearUsers()

    @Update
    fun updateUser(user: DatabaseUser)

    //POST

    @Query("SELECT * FROM post_table")
    fun getPosts(): LiveData<List<DatabasePost>>

    @Insert
    fun insertPost(post: DatabasePost)

    @Query("DELETE FROM post_table")
    fun clearPosts()

    @Query("SELECT * FROM post_table WHERE postId = :postId")
    fun getPostById(postId: Int): DatabasePost

    @Update
    fun updatePost(post: DatabasePost)

    @Delete
    fun deletePost(post: DatabasePost)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<DatabasePost>)

    @Query("SELECT * FROM post_table WHERE emailUser = :email")
    fun getOwnPosts(email: String): List<DatabasePost>

    @Transaction
    @Query("SELECT * FROM post_table")
    fun getPostWithReactions(): List<PostWithReactions>

    @Transaction
    @Query("SELECT * FROM post_table WHERE postId NOT IN (:favorites)")
    fun getFavoritesWithReactions(favorites: List<Int>): List<PostWithReactions>

    //FAVORITE

    @Transaction
    @Query("SELECT * FROM user_table WHERE email = :email")
    fun getUserWithFavorites(email: String): UserWithPosts

    @Delete
    fun deleteFavorite(favorite: UserFavoritePostsCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertFavorite(favorite: UserFavoritePostsCrossRef)

    //INBOX

    @Transaction
    @Query("SELECT * FROM user_table WHERE email = :email")
    fun getUserWithInbox(email: String): UserWithInbox

    @Insert
    fun deleteInbox(inbox: UserInboxPostsCrossRef)

    //REACTION

    @Insert
    fun insertReaction(reaction: DatabaseReaction)

    @Query("SELECT * FROM reaction_table WHERE reactionId = :reactionId")
    fun getReactionById(reactionId: Int): DatabaseReaction

    @Delete
    fun deleteReaction(reaction: DatabaseReaction)

    @Query("SELECT * FROM reaction_table")
    fun getReactions(): LiveData<List<DatabaseReaction>>

    @Query("SELECT * FROM user_table")
    fun getUsers(): LiveData<List<DatabaseUser>>

}