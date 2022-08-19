package com.example.ep3faith.database

import androidx.lifecycle.LiveData
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

    @Insert
    public fun insertPost(post: Post)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public fun insertFavorite(favorite: UserFavoritePostsCrossRef)

    @Transaction
    @Query("SELECT * FROM user_table WHERE email = :email")
    public fun getUserWithFavorites(email: String): UserWithPosts

}