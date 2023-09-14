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

    suspend fun getShowUserFollowersFromFirebase(clickedUserLogin: String): List<User> {
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

                                    if (clickedUserLogin == favLogin) {
                                        userInfoList.add(
                                            User(
                                                userLogin,
                                                favLogin = favLogin,
                                                html_url = userHtml,
                                                avatar_url = avatarUrl
                                            )
                                        )
                                    }
                                }
                            }
                        }
                        continuation.resume(userInfoList)
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