package com.ex.github.Repository

import android.net.Uri
import android.util.Log
import com.ex.github.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject

class AccountRepository @Inject constructor(private var auth: FirebaseAuth) {

    fun getPhoto(phone: String): Task<Uri> {
        val storageReference = FirebaseStorage.getInstance()
        var imageReference = storageReference.reference.child("$phone.jpg")
        Log.d("xxURİRepo", imageReference.downloadUrl.toString())

        return (imageReference.let { imageReference.downloadUrl }
            ?: (R.drawable.account)) as Task<Uri>
    }

    fun updateUserProfilePhoto(phone: String, imageUri: Uri?): Task<Uri> {
        val storageReference = FirebaseStorage.getInstance()
        var imageReference = storageReference.reference.child("$phone.jpg")

        imageUri?.let { imageReference.putFile(it) }
        var profile = UserProfileChangeRequest.Builder()
            .setPhotoUri(imageUri)
            .build()

        auth.currentUser?.updateProfile(profile)
        return imageReference.downloadUrl

    }
}
