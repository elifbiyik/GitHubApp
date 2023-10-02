package com.ex.github.ViewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.AccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(var repository: AccountRepository) : ViewModel() {

    var userProfilePhotoMutableLiveData = MutableLiveData<Uri>()

    fun getPhoto(phone: String) {
        repository.getPhoto(phone).addOnSuccessListener {
            userProfilePhotoMutableLiveData.value = it
        }.addOnFailureListener { exception ->
            Log.e("AccountViewModel", "Hata: ${exception.message}", exception)
        }
    }

    fun updateUserProfilePhoto(phone: String, imageUri: Uri?) {
        repository.updateUserProfilePhoto(phone, imageUri).addOnSuccessListener {
            userProfilePhotoMutableLiveData.value = it
        }.addOnFailureListener { exception ->
            Log.e("AccountViewModel", "Hata: ${exception.message}", exception)
        }
    }

    suspend fun currentUser(): List<String> {
        return repository.currentUser()
    }

    suspend fun showUserFollowing(loginUser: String ): Int {
        var userSize = repository.showUserFollowing(loginUser).size
        return userSize
    }

    suspend fun showUserFollower(loginUser: String ): Int {
        var userSize = repository.showUserFollower(loginUser).size
        return userSize
    }

    fun signOut(){
        repository.signOut()
    }
}