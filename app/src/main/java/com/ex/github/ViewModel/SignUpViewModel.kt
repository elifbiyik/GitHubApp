package com.ex.github.ViewModel

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.SignUpRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    var repository: SignUpRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    var userMutableLiveData = MutableLiveData<Boolean>()

    suspend fun signUp(nameSurname: String, phone: String) {
        if (nameSurname.isNullOrBlank() || phone.isNullOrBlank()) {
            Toast.makeText(
                context,
                "Please enter your name or phone number",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val isValid = repository.signUp(nameSurname, phone)
            userMutableLiveData.value = isValid
        }
    }

    fun uploadProfilePhoto(phone: String, imageUri: Uri?) {
        repository.uploadProfilePhoto(phone, imageUri)
    }
}