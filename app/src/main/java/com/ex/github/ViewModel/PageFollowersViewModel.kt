package com.ex.github.ViewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.PageFollowersRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PageFollowersViewModel @Inject constructor(var repository: PageFollowersRepository) : ViewModel() {

    var currentUserFollowersMutableLiveData = MutableLiveData<List<User>> ()

    suspend fun getShowUserFollowers(currentUser : String): List<User> {
        var userFollowers = repository.getShowUserFollowers(currentUser)
        Log.d("xxxxxxx", userFollowers.toString())
        currentUserFollowersMutableLiveData.value = userFollowers

        return userFollowers
    }
}