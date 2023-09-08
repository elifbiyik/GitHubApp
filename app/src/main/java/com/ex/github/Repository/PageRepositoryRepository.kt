package com.ex.github.Repository

import android.annotation.SuppressLint
import android.util.Log
import com.ex.github.Api.ApiServise
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

class PageRepositoryRepository @Inject constructor(
    var apiServise: ApiServise,
    var database: FirebaseDatabase,
    var auth: FirebaseAuth
) {

    private val databaseReferenceRepository =
        database.getReference("Favorite Repository")

    suspend fun getShowUserRepository(clickedUserLogin: String): List<Repositories> {
        return apiServise.getShowUserRepository(clickedUserLogin)
    }

    suspend fun addFavoriteRepository(loginUser: String, clickedUserLogin: String, repositoryName: String) {
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

    suspend fun getAllList(loginUser: String) : ArrayList<Repositories> {
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
                                    i.child("repoIsWhose").getValue(String::class.java)!!
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
