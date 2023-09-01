package com.ex.github.Repository

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import com.ex.github.Api.ApiServise
import com.ex.github.Repositories
import com.ex.github.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class HomePageRepository @Inject constructor(var auth: FirebaseAuth, var apiServise: ApiServise, var database: FirebaseDatabase) {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("User")

    suspend fun getAllUsers(): List<User> {
        return apiServise.getAllUser()
    }

    suspend fun getAllRepositories(): List<Repositories> {
        return apiServise.getAllRepositories()
    }

    fun currentUser(): String? {

        var currentUserPhone = auth.currentUser?.phoneNumber.toString()
        val dataSnapshot = databaseReference.child(currentUserPhone)

// Telefon numarasına göre bilgileri alsın. Anasyfada kendi de listelensin.

        Log.d("auth.currentUser", auth.currentUser.toString())
        Log.d("auth.currentUseUidr", auth.currentUser?.uid.toString())
        Log.d("auth.currentUserPho", auth.currentUser?.phoneNumber.toString())
        Log.d("auth.currentUserPro", auth.currentUser?.providerId.toString())
        Log.d("auth.currentUserTena", auth.currentUser?.tenantId .toString())
        return auth.currentUser?.uid
    }
}