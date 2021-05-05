package com.ericg.groom.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val readAllData: LiveData<List<User>>
    private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun readData(): LiveData<List<User>>{
        return readAllData
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Default) {
                repository.addUser(user)
            }
        }
    }

    fun deleteUSer(user: User){
        viewModelScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Default) {
                repository.deleteUser(user)
            }
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun deleteAll(){
        viewModelScope.launch (Dispatchers.IO){
            withContext(Dispatchers.Default) {
                repository.deleteAll()
            }
        }
    }

    fun searchData(searchQuery: String): LiveData<List<User>>{
        return  repository.searchData(searchQuery)
    }
}