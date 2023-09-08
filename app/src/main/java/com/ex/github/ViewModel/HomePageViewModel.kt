package com.ex.github.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repositories
import com.ex.github.Repository.HomePageRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(var repository: HomePageRepository) : ViewModel() {

    var usersMutableLiveData = MutableLiveData<List<User>>()
    var repositoriesMutableLiveData = MutableLiveData<List<Repositories>>()
    var filteredUsersMutableLiveData = MutableLiveData<List<User>>()
    var filteredRepositoriesMutableLiveData = MutableLiveData<List<Repositories>>()
    var filteredMutableLiveData = MutableLiveData<List<Any>>()


    suspend fun getAllUsers(): List<User> {
        var x = repository.getAllUsersFromApi()
        var y = repository.getAllUsersFromFirebase()

        var list = x + y
        usersMutableLiveData.value = list

        return list
    }




    suspend fun getAllRepositories(): List<Repositories> {
        repositoriesMutableLiveData.value = repository.getAllRepositories()
        return repository.getAllRepositories()
    }

    suspend fun filterUsers(search: String) {
        var response = repository.getAllUsersFromApi()
        var filterUsers = response.filter { it.login?.contains(search, ignoreCase = true) == true }
        filteredUsersMutableLiveData.value = filterUsers
    }

    suspend fun filterRepositories(search: String) {
        var response = repository.getAllRepositories()
        var responseBody = response.filter { it.name.contains(search, ignoreCase = true) }
        filteredRepositoriesMutableLiveData.value = responseBody
    }

    suspend fun filter () {
        filteredMutableLiveData.value = listOf(filteredUsersMutableLiveData, filteredRepositoriesMutableLiveData)
    }
}
