package com.ex.github.ViewModel

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.DetailRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(var repository: DetailRepository) : ViewModel() {

    var currentUserMutableLiveData = MutableLiveData<User>()
    var firebaseUserMutableLiveData = MutableLiveData<User>()

    suspend fun getShowUserFromApi(clickedUserLogin: String): User {
        var user = repository.getShowUserFromApi(clickedUserLogin)
        currentUserMutableLiveData.value = user
        return user
    }

    suspend fun getShowUserFromFirebase(clickedUserLogin: String) : User {
        var user = repository.getShowUserFromFirebase(clickedUserLogin)
        firebaseUserMutableLiveData.value = user
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
        repository.removeFavoriteUser(loginUser, favUser)
    }

    suspend fun currentUser(): List<String> {
        return repository.currentUser()
    }
}