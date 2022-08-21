package com.example.ep3faith.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class, Post::class, UserFavoritePostsCrossRef::class, Reaction::class], version = 11, exportSchema = false)
abstract class FaithDatabase: RoomDatabase() {
    abstract val faithDatabaseDAO: FaithDatabaseDAO

    companion object{
        @Volatile
        private var INSTANCE: FaithDatabase? = null

        fun getInstance(context: Context): FaithDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if(instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FaithDatabase::class.java,
                        "faith_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }

    }
}