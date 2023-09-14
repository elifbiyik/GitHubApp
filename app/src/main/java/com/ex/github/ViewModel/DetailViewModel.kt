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
    var firebaseUserMutableLiveData = MutableLiveData<User>()
    var firebaseUserFollowingSizeMutableLiveData = MutableLiveData<ArrayList<String>>()
    var firebaseUserFollowerSizeMutableLiveData = MutableLiveData<ArrayList<String>>()

    suspend fun getShowUserFromApi(clickedUserLogin: String, context: Context): User {
        var user = repository.getShowUserFromApi(clickedUserLogin, context)
        currentUserMutableLiveData.value = user
        return user
    }

    suspend fun getShowUserFromFirebase(clickedUserPhoneNumber: String) : User {
        var user = repository.getShowUserFromFirebase("+90$clickedUserPhoneNumber")
        Log.d("xxxxxx", user.toString())
        firebaseUserMutableLiveData.value = user
        return user
    }

    suspend fun addFavoriteUser(
        loginUser: String,
        favUser: String,
        favHtml: String,
        favAvatar: String,
        clickedUserisFirebase : Boolean,
        phoneNumber : String,
        context: Context
    ): Boolean {

        var isAdd = repository.addFavoriteUser(loginUser, favUser, favHtml, favAvatar,clickedUserisFirebase, phoneNumber, context)
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

    suspend fun getShowUserFollowersSize(clickedUserLogin: String) : Int {
        var list = repository.followersListForSize (clickedUserLogin)
      Log.d("VMDetailLisst", list.toString())
        return list.size
    }

    suspend fun getShowUserFollowingSize(clickedUserLogin: String) : Int {
        var list = repository.followingListForSize (clickedUserLogin)
        return list.size
    }

    suspend fun getShowUserRepositorySize(clickedUserLogin: String) : Int {
        var list = repository.repositoryListForSize (clickedUserLogin)
        return list.size
    }}