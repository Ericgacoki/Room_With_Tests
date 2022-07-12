package com.ericg.groom.data

import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.filters.SmallTest
import com.ericg.groom.getOrAwaitValueTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
@SmallTest
class UserDatabaseTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: UserDatabase
    private lateinit var dao: UserDao

    @Before
    fun setUp(){
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = database.userDao
    }

    @After
    fun teardown(){
        database.close()
    }

    @Test
    fun insertItemToDatabase() = runTest {
        val userItem = User(id =1, firstName = "Eric", lastName = "g", age = 20)
        dao.addUser(userItem)

        val allUsers = dao.readAllData().getOrAwaitValueTest()
        assertThat(allUsers).contains(userItem)
    }

    @Test
    fun deleteUserItem() = runTest {
        val userItem = User(id =1, firstName = "Eric", lastName = "g", age = 20)
        dao.addUser(userItem).also { dao.deleteUser(userItem) }

        val allUsers = dao.readAllData().getOrAwaitValueTest()
        assertThat(allUsers).doesNotContain(userItem)
    }
}