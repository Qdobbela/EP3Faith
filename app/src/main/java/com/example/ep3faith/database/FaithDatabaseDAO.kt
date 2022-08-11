package com.example.ep3faith.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query

@Dao
interface FaithDatabaseDAO {

    //add new youngster
    @Insert
    fun insertYoungster(user: User)

    //update youngster
    @Update
    fun updateYoungster(user: User)

    //get youngster with id
    @Query("SELECT * FROM youngster_table where id = :id")
    fun getYoungster(id: Long): User?

    //delete youngster with id
    @Query("DELETE FROM youngster_table where id = :id")
    fun deleteYoungster(id: Long)



}