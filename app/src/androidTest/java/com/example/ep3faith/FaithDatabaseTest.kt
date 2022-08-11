package com.example.ep3faith

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.database.FaithDatabaseDAO
import com.example.ep3faith.database.User
import org.junit.Assert.assertEquals
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import timber.log.Timber
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
    fun insertAndGetYoungster(){
        val user = User()
        user.username = "Nicerdicer"
        faithDAO.insertYoungster(user)
        Timber.i(user.id.toString())
        val firstYoungster = faithDAO.getYoungster(1)
        Timber.i(firstYoungster.toString())
        assertEquals("Nicerdicer", firstYoungster?.username)
    }

    @After
    @Throws(IOException::class)
    fun closeDb(){
        db.close()
    }
}