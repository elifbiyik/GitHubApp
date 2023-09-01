package com.ex.github.ViewModel

import android.app.Activity
import android.content.Context
import android.telephony.PhoneNumberUtils
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ex.github.Repository.SignInRepository
import com.ex.github.Repository.SignUpRepository
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(var repository: SignInRepository) : ViewModel() {

    var userMutableLiveData = MutableLiveData<Boolean>()

    suspend fun verify(context: Context, activity: Activity, phone: String) {
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

}