package com.example.ep3faith.database

import androidx.room.*

@Dao
interface FaithDatabaseDAO {

    @Query("SELECT * FROM user_table WHERE email = :email")
    public fun getUserByEmail(email: String): User

    @Insert
    public fun insertUsers(users: List<User>)

    @Query("DELETE FROM user_table")
    public fun clearUsers()

    @Query("DELETE FROM post_table")
    public fun clearPosts()

    @Update
    public fun updateUser(user: User)

    @Insert
    public fun insertPosts(posts: List<Post>)

    @Query("SELECT * FROM post_table")
    public fun getPosts(): List<Post>

    @Query("SELECT * FROM post_table WHERE emailUser = :email")
    public fun getOwnPosts(email: String): List<Post>

    @Insert
    public fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public fun insertFavorite(favorite: UserFavoritePostsCrossRef)

    @Transaction
    @Query("SELECT * FROM user_table WHERE email = :email")
    public fun getUserWithFavorites(email: String): UserWithPosts

    @Delete
    fun deleteFavorite(favorite: UserFavoritePostsCrossRef)

    @Insert
    public fun insertReaction(reaction: Reaction)

    @Transaction
    @Query("SELECT * FROM post_table")
    fun getPostWithReactions(): List<PostWithReactions>


    @Transaction
    @Query("SELECT * FROM post_table WHERE postId IN (:favorites)")
    fun getFavoritesWithReactions(favorites: List<Int>): List<PostWithReactions>

    @Delete
    fun deletePost(post: Post)

    @Query("SELECT * FROM post_table WHERE postId = :postId")
    fun getPostById(postId: Int): Post

}