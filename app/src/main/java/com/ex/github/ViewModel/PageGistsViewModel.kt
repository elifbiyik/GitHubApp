package com.ex.github.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.PageGistsRepository
import com.ex.github.Repository.PageRepositoryRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PageGistsViewModel @Inject constructor(var repository: PageGistsRepository) : ViewModel() {

    var currentUserGistsMutableLiveData = MutableLiveData<List<User>>()

    suspend fun getShowUserGists (currentUser : String ): List<User> {

        var response = repository.getShowUserGists(currentUser)
        Log.d("xxxxxxx", response.toString())
        currentUserGistsMutableLiveData.value = response
        return response
    }
}