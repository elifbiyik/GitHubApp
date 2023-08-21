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

    suspend fun getShowUserRepository (currentUser : String ): List<Repositories> {

        var response = repository.getShowUserRepository(currentUser)
        Log.d("xxxxxxx", response.toString())
        currentUserRepositoryMutableLiveData.value = response
        return response
    }
}