package com.ex.github.Repository

import android.app.Activity
import android.util.Log
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignInRepository @Inject constructor(private val auth: FirebaseAuth) {

    lateinit var verificationId: String
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    // 2-  Kodu doğrula
    fun signIn(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
    }

    // 1- Kodu gönder
       fun verify(activity: Activity, phone: String) {
        verificationCallbacks()
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phone)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(mCallbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
       }

    fun verificationCallbacks() {
        mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                signIn(credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("Failed", "Failed")
            }
            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@SignInRepository.verificationId = verificationId
            }
        }
    }
}

