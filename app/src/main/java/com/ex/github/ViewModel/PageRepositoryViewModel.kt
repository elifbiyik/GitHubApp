package com.ex.github.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ex.github.Repositories
import com.ex.github.Repository.PageRepositoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PageRepositoryViewModel @Inject constructor(var repository: PageRepositoryRepository) : ViewModel() {

    var currentUserRepositoryMutableLiveData = MutableLiveData<List<Repositories>>()
    var currentUserFavoriteRepositoryMutableLiveData = MutableLiveData<List<Repositories>>()

    suspend fun getShowUserRepository (clickedUserLogin : String ): List<Repositories> {
        var response = repository.getShowUserRepository(clickedUserLogin)
        currentUserRepositoryMutableLiveData.value = response
        return response
    }

    fun addFavoriteRepository (loginUser : String, clickedUserLogin: String, repositoryName : String) {
        viewModelScope.launch {
            repository.addFavoriteRepository(loginUser, clickedUserLogin, repositoryName)
        }
    }

    fun deleteFavoriteRepository (loginUser : String, repositoryName: String) {
        viewModelScope.launch {
            repository.deleteFavoriteRepository(loginUser, repositoryName)
        }
    }

    suspend fun getAllList(loginUser: String) : ArrayList<Repositories> {
        var response = repository.getAllList(loginUser)
        currentUserFavoriteRepositoryMutableLiveData.value = response
        return response
    }

    suspend fun currentUser (): List<String> {
        return repository.currentUser()
    }
}