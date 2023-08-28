package com.ex.github.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repositories
import com.ex.github.Repository.PageRepositoryRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PageRepositoryViewModel @Inject constructor(var repository: PageRepositoryRepository) : ViewModel() {

    var currentUserRepositoryMutableLiveData = MutableLiveData<List<Repositories>>()

    suspend fun getShowUserRepository (clickedUserLogin : String ): List<Repositories> {
        var response = repository.getShowUserRepository(clickedUserLogin)
        currentUserRepositoryMutableLiveData.value = response
        return response
    }



    suspend fun addFavoriteRepository (loginUser : String, clickedUserLogin: String, repositoryName : String) {
        repository.addFavoriteRepository(loginUser, clickedUserLogin, repositoryName)
    }

    suspend fun deleteFavoriteRepository (loginUser : String, repositoryName: String) {
        repository.deleteFavoriteRepository(loginUser ,repositoryName)
    }

    suspend fun getAllList(loginUser: String) : ArrayList<Repositories> {
        return repository.getAllList(loginUser)
    }


}