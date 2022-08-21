package com.example.ep3faith

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.database.FaithDatabaseDAO
import com.example.ep3faith.database.post.DatabasePost
import com.example.ep3faith.database.user.DatabaseUser
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.lang.Exception
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class FaithDatabaseTest {

    private lateinit var faithDAO: FaithDatabaseDAO
    private lateinit var db: FaithDatabase

    @Before
    fun createDb(){
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, FaithDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        faithDAO = db.faithDatabaseDAO
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUser(){
        val user = listOf(DatabaseUser("quinten.dobbelaere@gmail.com", "NicerDicer", ""))
        faithDAO.insertUsers(user)
        val firstYoungster = faithDAO.getUserByEmail("quinten.dobbelaere@gmail.com")
        assertEquals("NicerDicer", firstYoungster.username)
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }
}