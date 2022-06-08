package com.ericg.groom.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor( // A shared viewModel
    private val repository: UserRepository
) : ViewModel() {
    private lateinit var _readAllData: LiveData<List<User>>
    init {
        readData()
    }
    val data:  LiveData<List<User>> = _readAllData

    private fun readData() {
        _readAllData = repository.readAllData
    }

    fun addUser(user: User) {
        viewModelScope.launch() {
            repository.addUser(user)
        }
    }

    fun deleteUSer(user: User) {
        viewModelScope.launch {
            repository.deleteUser(user)
        }.invokeOnCompletion {
            readData()
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun searchData(searchQuery: String): LiveData<List<User>> {
        return repository.searchData(searchQuery)
    }
}