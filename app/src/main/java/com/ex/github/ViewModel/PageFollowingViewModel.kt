package com.ex.github.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.PageFollowingRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PageFollowingViewModel @Inject constructor(var repository: PageFollowingRepository) : ViewModel() {

    var currentUserFollowingMutableLiveData = MutableLiveData<List<User>>()

    suspend fun getShowUserFollowing (currentUser : String ): List<User> {

        var response = repository.getShowUserFollowing(currentUser)
        Log.d("xxxxxxx", response.toString())
        currentUserFollowingMutableLiveData.value = response
        return response
    }
}