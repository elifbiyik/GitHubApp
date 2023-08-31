package com.ex.github.Repository

import android.view.View
import com.ex.github.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import javax.inject.Inject

class SignUpRepository @Inject constructor(private val auth: FirebaseAuth) {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("User")

    suspend fun signUp(nameSurname: String, phone: String): Boolean {
        try {
            val user = User(nameSurname, phone)
            databaseReference.child("+90${phone}").setValue(user)
            return true
        } catch (e: Exception) {
            return false
        }
    }
}
