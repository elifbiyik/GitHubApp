package com.ex.github.Repository

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.ex.github.Api.ApiServise
import com.ex.github.R
import com.ex.github.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PageFollowersRepository @Inject constructor(
    var apiServise: ApiServise,
    var database: FirebaseDatabase
) {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("Favorite User")


    private fun showAlertDialog(context: Context) {
        val alertDialogBuilder = AlertDialog.Builder(context, R.style.CustomAlertDialog)
        alertDialogBuilder.setTitle("Error")
        alertDialogBuilder.setMessage("Too many requests")

        alertDialogBuilder.setPositiveButton("OK") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }


    // try içi çalıştığı halde hata veriyor crash oluyor ??
    suspend fun getShowUserFollowersApi(clickedUserLogin: String, context: Context): List<User> {
        try {
            var x = apiServise.getShowUserFollowers(clickedUserLogin)
            Log.d("getShowUserFollowersApi", x.toString())
        } catch (e: Exception) {
            Log.d("getShowUserFollowersApiCatch", e.message.toString())
            showAlertDialog(context)
        }

        return apiServise.getShowUserFollowers(clickedUserLogin)
    }

    suspend fun getShowUserFollowersFromFirebasee(clickedUserLogin: String): List<User> {
        return suspendCoroutine { continuation ->
            try {
                //    val userInfoList: ArrayList<User> = ArrayList()
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val userPromises = mutableListOf<Deferred<User?>>()
                        if (snapshot.exists()) {
                            for (j in snapshot.children) {
                                for (i in j.children) {
                                    var userLogin =
                                        i.child("login").getValue(String::class.java)
                                    var userHtml =
                                        i.child("html_url").getValue(String::class.java)
                                    var favLogin = i.child("favLogin").getValue(String::class.java)
                                    var avatarUrl =
                                        i.child("avatar_url").getValue(String::class.java)
                                    var loginPhone =
                                        i.child("loginPhone").getValue(String::class.java)
                                    //           var phoneNumber = i.child("phoneNumber")?.value?.toString()


                                    val userDeferred = GlobalScope.async {
                                        try {
                                            if (clickedUserLogin == favLogin) {
                                                val storageReference = FirebaseStorage.getInstance()
                                                var storageRef =
                                                    storageReference.reference.child("+90$loginPhone.jpg")
                                                val downloadUrl = storageRef.downloadUrl.await()
                                                User(
                                                    userLogin,
                                                    favLogin = favLogin,
                                                    //      login_avatar_url = downloadUrl,
                                                    html_url = userHtml,
                                                    avatar_url = avatarUrl,
                                                    //                 phoneNumber = phoneNumber
                                                )
                                            } else {
                                                User()
                                            }
                                        } catch (e: Exception) {
                                            Log.e("Hata", e.message.toString())
                                            null
                                        }
                                    }
                                    userPromises.add(userDeferred)
                                }
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

    suspend fun getShowUserFollowersFromFirebaseee(clickedUserLogin: String): List<User> {
        return suspendCoroutine { continuation ->
            try {
                val userInfoList: ArrayList<User> = ArrayList()
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (j in snapshot.children) {
                                for (i in j.children) {
                                    var userLogin =
                                        i.child("login").getValue(String::class.java)
                                    var userHtml =
                                        i.child("html_url").getValue(String::class.java)
                                    var favLogin = i.child("favLogin").getValue(String::class.java)
                                    var avatarUrl =
                                        i.child("avatar_url").getValue(String::class.java)
                                    var loginPhone =
                                        i.child("loginPhone").getValue(String::class.java)
                                    //           var phoneNumber = i.child("phoneNumber")?.value?.toString()


                                    val storageReference = FirebaseStorage.getInstance()
                                    var storageRef =
                                        storageReference.reference.child("+90$loginPhone.jpg")

                                    storageRef.downloadUrl.addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val downUrl = it.result
                                            if (clickedUserLogin == favLogin) {
                                                userInfoList.add(
                                                    User(
                                                        userLogin,
                                                        favLogin = favLogin,
                                                        //                login_avatar_url = downUrl,
                                                        html_url = userHtml,
                                                        avatar_url = avatarUrl
                                                    )
                                                )
                                            }
                                            continuation.resume(userInfoList)
                                        } else {
                                            continuation.resumeWithException(
                                                it.exception ?: Exception("Unknown error")
                                            )
                                        }
                                    }


                                    /* var downloadUrl = storageRef.downloadUrl.await()

                                     if (clickedUserLogin == favLogin) {
                                         userInfoList.add(
                                             User(
                                                 userLogin,
                                                 favLogin = favLogin,
                                                 login_avatar_url = downloadUrl,
                                                 html_url = userHtml,
                                                 avatar_url = avatarUrl,
                                                 //                 phoneNumber = phoneNumber
                                             )
                                         )
                                     }*/
                                }
                            }
                        }
                        //          continuation.resume(userInfoList)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException())
                    }
                }
                Log.d("xxxxxxxxxcxc", userInfoList.toString())
                databaseReference.addListenerForSingleValueEvent(getData)
            } catch (e: Exception) {
                Log.d("Hata", e.message.toString())
                continuation.resumeWithException(e)
            }
        }
    }

    suspend fun getShowUserFollowersFromFirebase(clickedUserLogin: String): List<User> {
        return suspendCoroutine { continuation ->
            try {
                CoroutineScope(Dispatchers.IO).launch {
                    val userInfoList: ArrayList<User> = ArrayList()
                    val getData = object : ValueEventListener {
                        @SuppressLint("SuspiciousIndentation")
                        override fun onDataChange(snapshot: DataSnapshot) {

                            if (snapshot.exists()) {
                                for (j in snapshot.children) {
                                    for (i in j.children) {
                                        var userLogin =
                                            i.child("login").getValue(String::class.java)
                                        var userHtml =
                                            i.child("html_url").getValue(String::class.java)
                                        var favLogin =
                                            i.child("favLogin").getValue(String::class.java)
                                        var avatarUrl =
                                            i.child("avatar_url").getValue(String::class.java)
                                        var loginPhone =
                                            i.child("loginPhone").getValue(String::class.java)
                                        //           var phoneNumber = i.child("phoneNumber")?.value?.toString()

                                        if (clickedUserLogin == favLogin) {
/*                                            val storageRef = FirebaseStorage.getInstance().reference
                                            val photoRef = storageRef.child("+90$loginPhone.jpg")
                                            photoRef.downloadUrl.addOnSuccessListener {
                                                userInfoList.add(
                                                    User(
                                                        userLogin,
                                                        favLogin = favLogin,
                                                        login_avatar_url = it,
                                                        html_url = userHtml,
                                                        avatar_url = avatarUrl,
                                                        //                 phoneNumber = phoneNumber
                                                    )
                                                )
                                                Log.d("xxxxxxxcxcxcAAAA", it.toString())
                                                Log.d("xxxxxxxcxcxcAAAAA", userInfoList.toString())
                                            }.addOnFailureListener {
                                              */  userInfoList.add(
                                                    User(
                                                        userLogin,
                                                        favLogin = favLogin,
                                                        html_url = userHtml,
                                                        avatar_url = avatarUrl,
                                                        //                 phoneNumber = phoneNumber
                                                    )
                                                )
                                //            }
                                        }
                                    }
                                }
                                continuation.resume(userInfoList)

                            } else {
                                continuation.resume(ArrayList())
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            continuation.resumeWithException(error.toException())
                        }
                    }
                    databaseReference.addListenerForSingleValueEvent(getData)
                }
            } catch (e: Exception) {
                Log.d("Hata", e.message.toString())
                continuation.resumeWithException(e)
            }
        }
    }


    suspend fun photo(loginPhone: String) {
        val list: ArrayList<User> = ArrayList()
        val storageReference = FirebaseStorage.getInstance()
        val storageRef = storageReference.reference.child("+90$loginPhone.jpg")

        var photo = storageRef.downloadUrl.await()


    }


}

