package com.ericg.groom.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table ORDER BY id ASC")
    fun readAllData(): LiveData<List<User>>

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Delete
    suspend fun deleteUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * FROM user_table WHERE firstName LIKE :searchQuery OR lastName LIKE :searchQuery")
    fun searchData(searchQuery: String): LiveData<List<User>>
}