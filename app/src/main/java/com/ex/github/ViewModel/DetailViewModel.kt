package com.ex.github.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.DetailRepository
import com.ex.github.User
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(var repository: DetailRepository) : ViewModel (){

    var currentUserMutableLiveData = MutableLiveData<User>()

   suspend fun getShowUser(currentUser : String): User {
       var user = repository.getShowUser(currentUser)
        currentUserMutableLiveData.value = user
       return user
    }
}