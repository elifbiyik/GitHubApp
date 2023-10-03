package com.ex.github.ViewModel

import android.content.Context
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

    var usersMutableLiveData = MutableLiveData<List<User>?>()
    var repositoriesMutableLiveData = MutableLiveData<List<Repositories>?>()
    var filteredUsersMutableLiveData = MutableLiveData<List<User>>()
    var filteredRepositoriesMutableLiveData = MutableLiveData<List<Repositories>?>()

    suspend fun getAllUsersFromFirebase () : List<User>?{
        var x = repository.getAllUsersFromFirebase()
        var list: List<User>? = null
        if (x != null) {
            list = x
        }
        usersMutableLiveData.value = list
        return list
    }

    suspend fun getAllUsersFromApi (context: Context) : List<User>?{
        var x = repository.getAllUsersFromApi(context)
        var list: List<User>? = null
        if (x != null) {
            list = x
        }
        usersMutableLiveData.value = list
        return list
    }

    suspend fun getAllRepositories(context: Context): List<Repositories>? {

        var x = repository.getAllRepositories(context)
        var list: List<Repositories>? = null
        if (x != null) {
            list = x
        }
        repositoriesMutableLiveData.value =  list
        return list
    }

    suspend fun filterUsers(search: String, context: Context) {

        var firebase = repository.getAllUsersFromFirebase()
        var api = repository.getAllUsersFromApi(context)
        var response: List<User>? = null
        if (api != null) {
            response = api + firebase
        } else {
            response = firebase

        }
        var filterUsers =
            response.filter { (it.login?.contains(search, ignoreCase = true) ?: null) as Boolean }
        filteredUsersMutableLiveData.value = filterUsers
    }

    suspend fun filterRepositories(search: String, context: Context) {

        var x = repository.getAllRepositories(context)
        var response: List<Repositories>? = null
        if(x != null) {
            response = x
        }
        var responseBody =
            response?.filter { (it.name?.contains(search, ignoreCase = true) ?: null) as Boolean }
        filteredRepositoriesMutableLiveData.value = responseBody
    }
}
