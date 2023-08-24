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

    suspend fun getShowUser(currentUser: String): User {
        var user = repository.getShowUser(currentUser)
        currentUserMutableLiveData.value = user
        return user
    }


    suspend fun addFavoriteUser(
        login: String,
        favUser: String,
        favHtml: String,
        favAvatar: String,
        context: Context
    ): Boolean {

        var x = repository.addFavoriteUser(login, favUser, favHtml, favAvatar, context)
        Log.d("xxxVM", x.toString())
        return x
    }

    suspend fun showFavoriteUser(
        login: String,
        context: Context
    ): ArrayList<String> {
        return repository.showFavoriteUser(login, context)
    }

    suspend fun removeFavoriteUser(
        login: String,
        favUser: String,
    ) {
        var x = repository.removeFavoriteUser(login, favUser)
        Log.d("xxxVM", x.toString())
    }

}