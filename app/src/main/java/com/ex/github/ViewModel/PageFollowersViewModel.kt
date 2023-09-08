package com.ex.github.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.PageFollowersRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PageFollowersViewModel @Inject constructor(var repository: PageFollowersRepository) : ViewModel() {

    var currentUserFollowersMutableLiveData = MutableLiveData<List<User>> ()

    suspend fun getShowUserFollowers(clickedUserLogin : String): List<User> {
        var userFollowers = repository.getShowUserFollowers(clickedUserLogin)
        currentUserFollowersMutableLiveData.value = userFollowers
        return userFollowers
    }
}