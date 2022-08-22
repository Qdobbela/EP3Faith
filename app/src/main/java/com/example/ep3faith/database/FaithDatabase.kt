package com.example.ep3faith.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ep3faith.database.post.DatabasePost
import com.example.ep3faith.database.reaction.DatabaseReaction
import com.example.ep3faith.database.user.DatabaseUser
import com.example.ep3faith.database.user.UserFavoritePostsCrossRef
import com.example.ep3faith.database.user.UserInboxPostsCrossRef

@Database(entities = [DatabaseUser::class, DatabasePost::class, UserFavoritePostsCrossRef::class, UserInboxPostsCrossRef::class,DatabaseReaction::class], version = 14, exportSchema = false)
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
//