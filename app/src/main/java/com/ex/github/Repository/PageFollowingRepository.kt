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
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PageFollowingRepository @Inject constructor(
    var apiServise: ApiServise,
    var database: FirebaseDatabase
) {

    private val databaseReferenceUser = database.getReference("Favorite User")


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


    suspend fun getShowUserFollowing(clickedUserLogin: String, context: Context): List<User> {

        try {
            var x = apiServise.getShowUserFollowing(clickedUserLogin)
            Log.d("getShowUserFollowing", x.toString())
        } catch (e: Exception) {
            Log.d("getShowUserFollowingCatch", e.message.toString())
            showAlertDialog(context)
        }

        return apiServise.getShowUserFollowing(clickedUserLogin)
    }


    suspend fun getShowUserFollowingFromFirebase(clickedUserLogin: String): List<User> {
        return suspendCoroutine { continuation ->
            try {
                val userList: ArrayList<User> = ArrayList()
                val databaseReference = databaseReferenceUser.child(clickedUserLogin)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val userLogin = i.child("favLogin").getValue(String::class.java)
                                var userAvatar = i.child("avatar_url").getValue(String::class.java)
                                var userHtml = i.child("html_url").getValue(String::class.java)

                                userList.add(
                                    User(
                                        favLogin = userLogin,
                                        html_url = userHtml,
                                        avatar_url = userAvatar
                                    )
                                )
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


}