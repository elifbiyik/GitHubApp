package com.ex.github.Repository

import android.annotation.SuppressLint
import android.util.Log
import com.ex.github.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FavoriteUserRepository @Inject constructor(
    private var database: FirebaseDatabase,
    var auth: FirebaseAuth
) {

    // Storage'dan görsel alabilmek için Firebase ve Api Userları olarak ayrıldı.

    private val databaseReferenceUser = database.getReference("Favorite User")

    suspend fun showFavoriteUserFirebase(
        loginUser: String,
    ): List<User> {
        return suspendCoroutine { continuation ->
            try {
                val userPromises = mutableListOf<Deferred<User?>>()

                val databaseReference = databaseReferenceUser.child(loginUser)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val login = i.child("login").getValue(String::class.java)
                                val favHtml = i.child("html_url").getValue(String::class.java)
                                val favAvatar = i.child("avatar_url").getValue(String::class.java)
                                val favLogin = i.child("favLogin").getValue(String::class.java)
                                val favPhoneNumber =
                                    i.child("phoneNumber").getValue(String::class.java)

                                val storageReference = FirebaseStorage.getInstance()
                                var storageRef =
                                    storageReference.reference.child("+90$favPhoneNumber.jpg")

                                val userDeferred = GlobalScope.async {
                                    try {
                                        val downloadUrl = storageRef.downloadUrl.await()
                                        User(
                                            login = login,
                                            avatar_url = favAvatar,
                                            html_url = favHtml,
                                            favLogin = favLogin,
                                            storage = downloadUrl
                                        )

                                    } catch (e: Exception) {
                                        Log.e("Hata", e.message.toString())
                                        null
                                    }
                                }
                                userPromises.add(userDeferred)
                            }

                            GlobalScope.launch(Dispatchers.Main) {
                                val usersList = userPromises.awaitAll().filterNotNull()
                                continuation.resume(usersList)
                            }
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



    suspend fun showFavoriteUserApi(loginUser: String): ArrayList<User> {
        return withContext(Dispatchers.IO) {
            val userList: ArrayList<User> = ArrayList()
            val databaseReference = databaseReferenceUser.child(loginUser)
            val snapshot = databaseReference.get().await()

            if (snapshot.exists()) {
                for (i in snapshot.children) {
                    val login = i.child("login").getValue(String::class.java)
                    val favLogin = i.child("favLogin").getValue(String::class.java)
                    val favHtml = i.child("html_url").getValue(String::class.java)
                    val favAvatar = i.child("avatar_url").getValue(String::class.java)
                    val favIsFirebase = i.child("firebase").getValue(Boolean::class.java)

                    if(favIsFirebase == false) {
                        userList.add(
                            User(
                                login = login,
                                avatar_url = favAvatar,
                                html_url = favHtml,
                                favLogin = favLogin
                            )
                        )
                    }
                }
            }
            return@withContext userList
        }
    }




    suspend fun showFavoriteUserApi1(
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
                                val login = i.child("login").getValue(String::class.java)
                                val favLogin = i.child("favLogin").getValue(String::class.java)
                                val favHtml = i.child("html_url").getValue(String::class.java)
                                val favAvatar = i.child("avatar_url").getValue(String::class.java)
                                val favIsFirebase = i.child("firebase").getValue(Boolean::class.java)

                                if(favIsFirebase == false) {
                                    userList.add(
                                        User(
                                            login = login,
                                            avatar_url = favAvatar,
                                            html_url = favHtml,
                                            favLogin = favLogin
                                        )
                                    )
                                }

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

    suspend fun currentUser(): List<String> {
        var currentUserPhone = auth.currentUser?.phoneNumber.toString()
        return suspendCoroutine { continuation ->
            try {
                val userInfoList: ArrayList<String> = ArrayList()
                val databaseReference =
                    FirebaseDatabase.getInstance().getReference("User")
                        .child(currentUserPhone)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var currentUserLogin =
                                snapshot.child("login").getValue(String::class.java)
                                    .toString()
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
}
