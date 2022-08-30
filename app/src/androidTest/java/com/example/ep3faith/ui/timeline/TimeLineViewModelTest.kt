package com.example.ep3faith.ui.timeline

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.database.FaithDatabaseDAO
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TimeLineViewModelTest {

    private lateinit var faithDAO: FaithDatabaseDAO
    private lateinit var db: FaithDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, FaithDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        faithDAO = db.faithDatabaseDAO
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun getUser() {
        assertNotNull(faithDAO.getUsers())
    }

    @Test
    fun getPosts() {
        assertNotNull(faithDAO.getPosts())
    }
}
