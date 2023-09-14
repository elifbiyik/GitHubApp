package com.ex.github.Repository

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import com.ex.github.Api.ApiServise
import com.ex.github.R
import com.ex.github.Repositories
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

class PageRepositoryRepository @Inject constructor(
    var apiServise: ApiServise,
    var database: FirebaseDatabase,
    var auth: FirebaseAuth
) {

    private val databaseReferenceRepository =
        database.getReference("Favorite Repository")


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


    suspend fun getShowUserRepository(
        clickedUserLogin: String,
        context: Context
    ): List<Repositories> {

        try {
            var x = apiServise.getShowUserRepository(clickedUserLogin)
            Log.d("getShowUserRepository", x.toString())
        } catch (e: Exception) {
            Log.d("getShowUserRepositoryCatch", e.message.toString())
            showAlertDialog(context)
        }

        return apiServise.getShowUserRepository(clickedUserLogin)
    }

    suspend fun getShowUserRepositoryFromFirebase(
        clickedUserLogin: String,
    ): List<Repositories> {
        return suspendCoroutine { continuation ->
            try {
                val repoList: ArrayList<Repositories> = ArrayList()
                val databaseReference = databaseReferenceRepository.child(clickedUserLogin)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (j in snapshot.children) {
                                val userLogin = j.child("login").getValue(String::class.java)
                                val userFullName = j.child("full_name").getValue(String::class.java)
                                val repoName = j.child("name").getValue(String::class.java)

                                repoList.add(
                                    Repositories(
                                        addFavWhose = userLogin,
                                        full_name = userFullName,
                                        name = repoName
                                    )
                                )
                            }
                            continuation.resume(repoList)
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


    suspend fun addFavoriteRepository(
        loginUser: String,
        clickedUserLogin: String,
        repositoryName: String
    ) {
        try {
            val repository = Repositories(repositoryName, clickedUserLogin, null, null, null, null)
            databaseReferenceRepository.child(loginUser).child(repositoryName).setValue(repository)
        } catch (e: Exception) {
            Log.d("Hata", e.message.toString())
        }
    }

    suspend fun deleteFavoriteRepository(loginUser: String, repositoryName: String) {
        try {
            databaseReferenceRepository.child(loginUser).child(repositoryName).removeValue()
        } catch (e: Exception) {
            Log.d("Hata", e.message.toString())
        }
    }

    suspend fun getAllList(loginUser: String): ArrayList<Repositories> {
        return suspendCoroutine { continuation ->
            try {
                val favRepoList: ArrayList<Repositories> = ArrayList()
                var favRepo = databaseReferenceRepository.child(loginUser)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                val repoName = i.child("name").getValue(String::class.java)!!
                                val repoIsWhose =
                                    i.child("full_name").getValue(String::class.java)!!
                                favRepoList.add(
                                    Repositories(
                                        repoName,
                                        repoIsWhose,
                                        null,
                                        null,
                                        null,
                                        null
                                    )
                                )
                            }
                            continuation.resume(favRepoList)
                        } else {
                            continuation.resume(ArrayList())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException())
                    }
                }
                favRepo.addListenerForSingleValueEvent(getData)
            } catch (e: Exception) {
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
}
