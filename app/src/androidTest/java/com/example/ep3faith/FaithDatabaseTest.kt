package com.example.ep3faith

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.ep3faith.database.FaithDatabase
import com.example.ep3faith.database.FaithDatabaseDAO
import com.example.ep3faith.database.reaction.DatabaseReaction
import com.example.ep3faith.database.user.DatabaseUser
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
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
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, FaithDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        faithDAO = db.faithDatabaseDAO
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetUser() {
        val user = listOf(DatabaseUser("quinten.dobbelaere@gmail.com", "NicerDicer", ""))
        faithDAO.insertUsers(user)
        val firstYoungster = faithDAO.getUserByEmail("quinten.dobbelaere@gmail.com")
        assertEquals("NicerDicer", firstYoungster.username)
    }

    @Test
    @Throws(Exception::class)
    fun deleteReactionDeletesReaction() {
        val reaction = DatabaseReaction(0, "Hello", "NicerDicer", 0, "quinten.dobbelaere@student.hogent.be")
        faithDAO.insertReaction(reaction)
        faithDAO.deleteReaction(reaction)
        assertNull(faithDAO.getReactionById(reactionId = 0))
    }

    @Test
    @Throws(Exception::class)
    fun updateUserUpdatesUser() {
        val user = listOf(DatabaseUser("quinten.dobbelaere@gmail.com", "NicerDicer", ""))
        faithDAO.insertUsers(user)
        user.first().username = "tryhard"
        faithDAO.updateUser(user.first())
        assertEquals(faithDAO.getUserByEmail("quinten.dobbelaere@gmail.com").username, "tryhard")
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }
}
