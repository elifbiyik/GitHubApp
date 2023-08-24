package com.ex.github.Repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ex.github.Api.ApiServise
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

class DetailRepository @Inject constructor(
    var apiServise: ApiServise,
    var database: FirebaseDatabase
) {


    private val databaseReferenceUser =
        database.getReference("User")


    suspend fun getShowUser(currentUser: String): User {
        return apiServise.getShowUser(currentUser)
    }


    // ValueEventListener kullanıldığında; callback veya asenkron kullan.

    suspend fun showFavoriteUser(
        login: String,
        context: Context
    ): ArrayList<String> {

        // suspendCoroutine -> Asenkron işlem sonuçlarını dön.
        return suspendCoroutine { continuation ->
            try {
                val userList: ArrayList<String> = ArrayList()
                val databaseReference = database.getReference("User/${login}")
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val favUser = i.child("login").getValue(String::class.java)!!
                                userList.add(favUser)
                            }
                            continuation.resume(userList)
                            // resume ->  Sonucu dön.
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, "You can't ! ", Toast.LENGTH_SHORT).show()
                        continuation.resumeWithException(error.toException())
                        // resumeWithException -> Hata durumu
                    }
                }
                databaseReference.addListenerForSingleValueEvent(getData)
            } catch (e: Exception) {
                Log.d("Hata", e.message.toString())
                continuation.resumeWithException(e)
            }
        }
    }


    // login -> giriş yapılan kullanıcı
    // favUser -> Favorilere eklenmek istenen kullanıcı
    suspend fun addFavoriteUser(
        login: String,
        favUser: String,
        favHtml: String,
        favAvatar: String,
        context: Context
    ): Boolean {
        try {
            var isValid = false
            var userList: ArrayList<String> = ArrayList()

            var databaseReference = database.getReference("User/${login}")
            var getData = object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            var favUser = i.child("login").getValue(String::class.java)!!
                            userList.add(favUser)
                        }
                    }
                    if (userList.contains(favUser)) {
                        Toast.makeText(context, "You can't", Toast.LENGTH_SHORT).show()
                        isValid = false
                    } else {
                        val favUsers = User(favUser, html_url = favHtml, avatar_url = favAvatar)
             //           val key = databaseReferenceUser.push().key!!
                        databaseReferenceUser.child(login).child(favUser).setValue(favUsers)
                        isValid = true
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, "You can't ! ", Toast.LENGTH_SHORT).show()
                }
            }
            databaseReference.addListenerForSingleValueEvent(getData)
            return isValid

        } catch (e: Exception) {
            Log.d("Hata", e.message.toString())
            return false
        }
    }


    suspend fun removeFavoriteUser(
        login: String,
        favUser: String
    ) {
        database.getReference("User/${login}").child(favUser).removeValue()
    }


}


