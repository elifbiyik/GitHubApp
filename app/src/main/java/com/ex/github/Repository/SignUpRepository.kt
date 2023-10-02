package com.ex.github.Repository

import android.net.Uri
import android.util.Log
import com.ex.github.R
import com.ex.github.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class SignUpRepository @Inject constructor(private val auth: FirebaseAuth) {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("User")

    suspend fun signUp(nameSurname: String, phone: String): Boolean {
        try {
            val user = User(nameSurname, phoneNumber = phone, isFirebase = true)
            databaseReference.child("+90${phone}").setValue(user)
            return true
        } catch (e: Exception) {
            return false
        }
    }

    fun uploadProfilePhoto (phone : String, imageUri: Uri?) {
        val storageReference = FirebaseStorage.getInstance()
        var imageReference = storageReference.reference.child("+90${phone}.jpg")
        imageUri?.let { imageReference.putFile(it) }
    }

    fun notUploadProfile(phone : String){
        val storageReference = FirebaseStorage.getInstance()
        var imageReference = storageReference.reference.child("+90${phone}.jpg")
        val image = R.drawable.account
        val imageUri = Uri.parse("android.resource://com.ex.github/$image")
       imageUri?.let {
           imageReference.putFile(it)
               .addOnSuccessListener {
                   Log.d("Upload", "Upload Okey")
               }
               .addOnFailureListener { e ->
                   Log.d("Upload", e.message.toString())
               }
       }
    }
}
