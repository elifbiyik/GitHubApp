package com.ex.github.Repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ex.github.Api.ApiServise
import com.ex.github.User
import com.google.firebase.auth.FirebaseAuth
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
    var database: FirebaseDatabase,
    var auth: FirebaseAuth
) {

    private val databaseReferenceUser = database.getReference("Favorite User")

    suspend fun getShowUserFromApi(clickedUserLogin: String): User {
        return apiServise.getShowUser(clickedUserLogin)
    }

    suspend fun getShowUserFromFirebase(clickedUserLogin: String): User {
        return suspendCoroutine { continuation ->
            try {
                val databaseReference =
                    FirebaseDatabase.getInstance().getReference("User").child(clickedUserLogin)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var currentUserLogin =
                                snapshot.child("login").getValue(String::class.java).toString()
                            var currentUserPhone =
                                snapshot.child("phoneNumber").getValue(String::class.java)
                                    .toString()

                            continuation.resume(User(currentUserLogin, phoneNumber = currentUserPhone))
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
                                snapshot.child("phoneNumber").getValue(String::class.java)
                                    .toString()

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
        context: Context
    ): ArrayList<String> {

        return suspendCoroutine { continuation ->
            try {
                val userList: ArrayList<String> = ArrayList()
                val databaseReference = databaseReferenceUser.child(loginUser)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val favUser = i.child("login").getValue(String::class.java)!!
                                userList.add(favUser)
                            }
                            continuation.resume(userList)
                        } else {
                            continuation.resume(ArrayList())
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

    // login -> giriş yapılan kullanıcı
    // favUser -> Favorilere eklenmek istenen kullanıcı
    suspend fun addFavoriteUser(
        loginUser: String,
        favUser: String,
        favHtml: String,
        favAvatar: String,
        context: Context
    ): Boolean {
        try {
            var isValid = false
            var userList: ArrayList<String> = ArrayList()

            val databaseReference = databaseReferenceUser.child(loginUser)
            var getData = object : ValueEventListener {
                @SuppressLint("SuspiciousIndentation")
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        for (i in snapshot.children) {
                            var favoriteUser = i.child("login").getValue(String::class.java)!!
                            userList.add(favoriteUser)
                        }
                    }
                    if (userList.contains(favUser)) {
                        Toast.makeText(context, "You can't", Toast.LENGTH_SHORT).show()
                        isValid = false
                    } else {
                        val favUsers = User(favUser, html_url = favHtml, avatar_url = favAvatar)
                        databaseReferenceUser.child(loginUser).child(favUser).setValue(favUsers)
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
        loginUser: String,
        favUser: String
    ) {
        databaseReferenceUser.child(loginUser).child(favUser).removeValue()
    }

}