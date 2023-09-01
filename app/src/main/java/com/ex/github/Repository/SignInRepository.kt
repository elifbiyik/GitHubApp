package com.ex.github.Repository

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class SignInRepository @Inject constructor(private val auth: FirebaseAuth) {

    var verificationId: String? = null
    var token: PhoneAuthProvider.ForceResendingToken? = null
    lateinit var mCallbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("User")

    fun getVerification(): String? {
        return verificationId
    }

    // 2-  Kodu doğrula
    suspend fun signIn(context:Context, credential: PhoneAuthCredential) : Boolean{
        return withContext(Dispatchers.Main) {
            try {
                val authResult = auth.signInWithCredential(credential).await()
                if (authResult.user != null) {
                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
                    true
                } else {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    false
                }
            } catch (e: Exception) {
                Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                false
            }
        }
    }
    fun signInCredential(credential: PhoneAuthCredential) {
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
                signInCredential(credential)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Log.d("Failed", "Failed")
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                this@SignInRepository.verificationId = verificationId
                this@SignInRepository.token = token
                Log.d("TOKEN", token.toString())
            }
        }
    }

    suspend fun isRegistered(phone: String): Boolean {
        val phoneNumberDb = databaseReference.child("+90$phone")
        val dataSnapshot = phoneNumberDb.get().await()
        dataSnapshot.exists()
        return try {
            if(dataSnapshot.value != null) {
                true
            }else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }
}

