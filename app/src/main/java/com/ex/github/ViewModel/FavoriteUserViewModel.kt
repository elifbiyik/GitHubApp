package com.ex.github.ViewModel

import android.content.Context
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
        context: Context
    ): ArrayList<User> {
        var users = repository.showFavoriteUser(loginUser, context)
        currentUserFavoriteUserMutableLiveData.value = users
        return users
    }
}