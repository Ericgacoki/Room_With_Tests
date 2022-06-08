package com.ericg.groom.data

import androidx.lifecycle.LiveData
import javax.inject.Inject

class UserRepository @Inject constructor(private val db: UserDatabase) {
    val readAllData: LiveData<List<User>> = db.userDao.readAllData()

    suspend fun addUser(user: User) {
        db.userDao.addUser(user)
    }

    suspend fun deleteUser(user: User) {
        db.userDao.deleteUser(user)
    }

    suspend fun updateUser(user: User) {
        db.userDao.updateUser(user)
    }

    suspend fun deleteAll() {
        db.userDao.deleteAll()
    }

    fun searchData(searchQuery: String): LiveData<List<User>> {
        return db.userDao.searchData(searchQuery)
    }
}