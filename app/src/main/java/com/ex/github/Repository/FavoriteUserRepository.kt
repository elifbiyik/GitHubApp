package com.ex.github.Repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ex.github.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FavoriteUserRepository @Inject constructor(private var database: FirebaseDatabase) {

    private val databaseReferenceUser = database.getReference("Favorite User")

    suspend fun showFavoriteUser(
        loginUser: String,
        context: Context
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
                                val favHtml = i.child("html_url").getValue(String::class.java)!!
                                val favAvatar = i.child("avatar_url").getValue(String::class.java)!!
                                userList.add(User(favUser, favAvatar, favHtml))
                            }
                            continuation.resume(userList)
                        } else {
                            continuation.resume(ArrayList()) // Boş liste döndürme
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "You can't ! ", Toast.LENGTH_SHORT).show()
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
}
