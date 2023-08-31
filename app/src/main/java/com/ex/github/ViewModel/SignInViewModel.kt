package com.ex.github.ViewModel

import android.app.Activity
import android.telephony.PhoneNumberUtils
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

    suspend fun verify( activity: Activity ,phone : String) {
        repository.verify(activity ,"+90$phone")
    }

    suspend fun signIn(credential: PhoneAuthCredential){
       /* var result = repository.signIn(credential)
        userMutableLiveData.value = result
        return result*/
    }




}