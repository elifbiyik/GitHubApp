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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Repo
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.Exception
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class HomePageRepository @Inject constructor(
    var auth: FirebaseAuth,
    var apiServise: ApiServise,
    var database: FirebaseDatabase
)  {

    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().getReference("User")

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


    suspend fun getAllUsersFromApi(context: Context): List<User>? {

        var x: List<User>? = null
        try {
             x = apiServise.getAllUser()
            Log.d("getAllUsersFromApi", x.toString())
        } catch (e: Exception) {
            Log.d("getAllUsersFromApiCatch", e.message.toString())
            showAlertDialog(context)
        }
        return x
    }

    suspend fun getAllRepositories(context: Context): List<Repositories>? {
        var x: List<Repositories>? = null
        try {
            x = apiServise.getAllRepositories()
            Log.d("getAllRepositories", x.toString())
        } catch (e: Exception) {
            Log.d("getAllRepositoriesCatch", e.message.toString())
            showAlertDialog(context)
        }
        return x
    }

    suspend fun getAllUsersFromFirebase(): List<User> {
        return suspendCancellableCoroutine { continuation ->
            try {
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {

                        val userPromises = mutableListOf<Deferred<User?>>()
// Deferred; bu tür asenkron işlemleri daha kolay yönetmemizi sağlar. Değeri sonradan alınacağını söyl.

                        if (snapshot.exists()) {
                            for (i in snapshot.children) {
                                var userLogin =
                                    i.child("login").getValue(String::class.java)
                                var userPhone =
                                    i.child("phoneNumber").getValue(String::class.java)

                                val storageReference = FirebaseStorage.getInstance()
                                var storageRef =
                                    storageReference.reference.child("+90$userPhone.jpg")

                                val userDeferred = GlobalScope.async {
                                    try {
                                        val downloadUrl = storageRef.downloadUrl.await()
                                        User(
                                            userLogin,
                                            phoneNumber = userPhone,
                                            isFirebase = true,
                                            storage = downloadUrl
                                        )
                                    } catch (e: Exception) {
                                        Log.e("Hata", e.message.toString())
                                        null
                                    }
                                }
                                // storage'dan sonuç dönüp dönmemesine bakmamız lazım o yüzden await kullan
                                // await kullandığın için async kullan.
                                userPromises.add(userDeferred)

                            }
                            GlobalScope.launch(Dispatchers.Main) {
                                val usersList = userPromises.awaitAll().filterNotNull()
                                continuation.resume(usersList)
                            }
                            // awaitAll = Tüm deferredları bekler ve donucu alır.

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
                Log.d("HatagetAllUsersFromFirebase", e.message.toString())
                continuation.resumeWithException(e)
            }
        }
    }

    suspend fun currentUser(): List<User> {
        var currentUserPhone = auth.currentUser?.phoneNumber.toString()

        return suspendCoroutine { continuation ->
            try {
                val userInfoList: ArrayList<User> = ArrayList()
                val databaseReference = databaseReference.child(currentUserPhone)
                val getData = object : ValueEventListener {
                    @SuppressLint("SuspiciousIndentation")
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            var currentUserLogin =
                                snapshot.child("login").getValue(String::class.java)
                            var currentUserPhone =
                                snapshot.child("phoneNumber").getValue(String::class.java)

                            userInfoList.add(
                                User(
                                    currentUserLogin,
                                    phoneNumber = currentUserPhone,
                                )
                            )

                            continuation.resume(userInfoList)
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        continuation.resumeWithException(error.toException())
                    }
                }
                databaseReference.addListenerForSingleValueEvent(getData)
            } catch (e: Exception) {
                Log.d("HatacurrentUser", e.message.toString())
                continuation.resumeWithException(e)
            }
        }
    }
}