package com.ex.github.ViewModel

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
    var filteredUsersMutableLiveData = MutableLiveData<List<User>>()
    var filteredRepositoriesMutableLiveData = MutableLiveData<List<Repositories>>()

    suspend fun getAllUsers(): List<User> {
        usersMutableLiveData.value = repository.getAllUsers()
        return repository.getAllUsers()
    }

    suspend fun filterUsers(search: String) {
        var response = repository.getAllUsers()
        var filterUsers = response.filter { it.login?.contains(search, ignoreCase = true) == true }
        filteredUsersMutableLiveData.value = filterUsers
    }

    suspend fun filterRepositories(search : String) {
        var response = repository.getAllRepositories()
        Log.d("xxxxx", response.toString())
        var responseBody = response.filter { it.name.contains(search,ignoreCase = true) }
        filteredRepositoriesMutableLiveData.value = responseBody
    }
}
