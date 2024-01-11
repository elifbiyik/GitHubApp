package com.ex.github.ViewModel

import android.app.Activity
import android.content.Context
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.SignInRepository
import com.google.firebase.auth.PhoneAuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(var repository: SignInRepository) : ViewModel() {

    suspend fun verify(activity: Activity, phone: String) {
        repository.verify(activity, "+90$phone")
    }

    fun getVerificationId(): String? {
        return repository.getVerification()
    }

    suspend  fun signInWithCredential(context: Context, credential: PhoneAuthCredential) : Boolean{
       return repository.signIn(context, credential)
    }

    suspend fun isRegistered(phone: String): Boolean {
        return repository.isRegistered(phone)
    }

    fun getCurrentUserUID (): String? {
        return repository.currentUser()
    }
}