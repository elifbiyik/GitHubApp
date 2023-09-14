package com.ex.github.Repository

import android.annotation.SuppressLint
import android.util.Log
import com.ex.github.Repositories
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

class FavoriteRepositoryRepository @Inject constructor(
    var database: FirebaseDatabase,
    var auth: FirebaseAuth
) {

    private val databaseReferenceRepository = database.getReference("Favorite Repository")

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
                                        name = repoName,
                                        full_name = repoIsWhose,
                                        null,
                                        null,
                                        null,
                                        null
                                    )
                                )
                            }
                            continuation.resume(favRepoList)
                        } else {
                            continuation.resume(ArrayList()) // Boş liste döndür
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