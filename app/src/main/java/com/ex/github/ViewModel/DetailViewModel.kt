package com.ex.github.ViewModel

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.DetailRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(var repository: DetailRepository) : ViewModel() {

    var currentUserMutableLiveData = MutableLiveData<User>()

    suspend fun getShowUser(clickedUserLogin: String): User {
        var user = repository.getShowUser(clickedUserLogin)
        currentUserMutableLiveData.value = user
        return user
    }


    suspend fun addFavoriteUser(
        loginUser: String,
        favUser: String,
        favHtml: String,
        favAvatar: String,
        context: Context
    ): Boolean {

        var isAdd = repository.addFavoriteUser(loginUser, favUser, favHtml, favAvatar, context)
        return isAdd
    }

    suspend fun showFavoriteUser(
        loginUser: String,
        context: Context
    ): ArrayList<String> {
        return repository.showFavoriteUser(loginUser, context)
    }

    suspend fun removeFavoriteUser(
        loginUser: String,
        favUser: String,
    ) {
        var x = repository.removeFavoriteUser(loginUser, favUser)
        Log.d("xxxVM", x.toString())
    }

}