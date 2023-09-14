package com.ex.github.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.PageFollowersRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PageFollowersViewModel @Inject constructor(var repository: PageFollowersRepository) : ViewModel() {

    var currentUserFollowersMutableLiveData = MutableLiveData<List<User>> ()
    var currentUserFollowersFromfirebaseMutableLiveData = MutableLiveData<List<User>> ()

    suspend fun getShowUserFollowersApi(clickedUserLogin : String, context : Context ): List<User> {
        var userFollowers = repository.getShowUserFollowersApi(clickedUserLogin, context)
        currentUserFollowersMutableLiveData.value = userFollowers
        return userFollowers
    }

    suspend fun getShowUserFollowersFromFirebase (clickedUserLogin: String): List<User> {
        var userFollowers = repository.getShowUserFollowersFromFirebase(clickedUserLogin)
        currentUserFollowersFromfirebaseMutableLiveData.value = userFollowers
        return userFollowers
    }


}