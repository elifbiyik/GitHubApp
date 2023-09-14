package com.ex.github.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.FavoriteUserRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteUserViewModel @Inject constructor(var repository: FavoriteUserRepository) : ViewModel(){

    var currentUserFavoriteUserMutableLiveData = MutableLiveData<List<User>> ()

    suspend fun showFavoriteUser(
        loginUser: String,
    ): List<User> {
        var usersFirebase = repository.showFavoriteUserFirebase(loginUser)
        var usersApi = repository.showFavoriteUserApi(loginUser)
        var users = usersFirebase + usersApi

        Log.d("FavUserVM", users.toString())

        currentUserFavoriteUserMutableLiveData.value = users
        return users
    }

    suspend fun currentUser (): List<String> {
        return repository.currentUser()
    }
}