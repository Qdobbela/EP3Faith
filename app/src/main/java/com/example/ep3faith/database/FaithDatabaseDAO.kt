package com.example.ep3faith.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query

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
}