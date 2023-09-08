package com.ex.github.Repository

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import com.ex.github.R
import com.ex.github.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class AccountRepository @Inject constructor(private var database: FirebaseDatabase, private var auth: FirebaseAuth) {

    private val databaseReferenceUser = database.getReference("Favorite User")

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

    suspend fun currentUser(): List<String> {
        var currentUserPhone = auth.currentUser?.phoneNumber.toString()
        return suspendCoroutine { continuation ->
            try {
                val userInfoList: ArrayList<String> = ArrayList()
                val databaseReference =
                    FirebaseDatabase.getInstance().getReference("User").child(currentUserPhone)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var currentUserLogin =
                                snapshot.child("login").getValue(String::class.java).toString()
                            var currentUserPhone =
                                snapshot.child("phoneNumber").getValue(String::class.java).toString()

                            userInfoList.add(currentUserLogin)
                            userInfoList.add(currentUserPhone)
                            continuation.resume(userInfoList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException())
                    }
                }
                databaseReference.addListenerForSingleValueEvent(getData)
            } catch (e: Exception) {
                Log.d("Hata", e.message.toString())
                continuation.resumeWithException(e)
            }
        }
    }

    suspend fun showFavoriteUser(
        loginUser: String,
    ): ArrayList<User> {
        return suspendCoroutine { continuation ->
            try {
                val userList: ArrayList<User> = ArrayList()
                val databaseReference = databaseReferenceUser.child(loginUser)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val favUser = i.child("login").getValue(String::class.java)!!
                                userList.add(User(favUser))
                            }
                            continuation.resume(userList)
                        } else {
                            continuation.resume(ArrayList())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException())
                    }
                }
                databaseReference.addListenerForSingleValueEvent(getData)
            } catch (e: Exception) {
                Log.d("Hata", e.message.toString())
                continuation.resumeWithException(e)
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }
}
