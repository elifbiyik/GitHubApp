package com.ex.github.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repositories
import com.ex.github.Repository.FavoriteRepositoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteRepositoryViewModel @Inject constructor(var repository: FavoriteRepositoryRepository)  : ViewModel() {

    var currentFavoriteRepositoryMutableLiveData = MutableLiveData<List<Repositories>>()

    suspend fun getAllList (loginUser : String ): List<Repositories> {
        var response = repository.getAllList(loginUser)
        currentFavoriteRepositoryMutableLiveData.value = response
        return response
    }


}