package com.example.ep3faith.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query

@Dao
interface FaithDatabaseDAO {

    //add new youngster
    @Insert
    fun insertYoungster(youngster: Youngster)

    //update youngster
    @Update
    fun updateYoungster(youngster: Youngster)

    //get youngster with id
    @Query("SELECT * FROM youngster_table where youngsterId = :id")
    fun getYoungster(id: Long): Youngster?

    //delete youngster with id
    @Query("DELETE FROM youngster_table where youngsterId = :id")
    fun deleteYoungster(id: Long)



}