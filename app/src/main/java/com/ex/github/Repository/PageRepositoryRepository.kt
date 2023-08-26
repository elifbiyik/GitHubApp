package com.ex.github.Repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.ex.github.Api.ApiServise
import com.ex.github.Repositories
import com.ex.github.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.core.Repo
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class PageRepositoryRepository @Inject constructor(
    var apiServise: ApiServise,
    var database: FirebaseDatabase
) {

    private val databaseReferenceRepository =
        database.getReference("Repository")


    suspend fun getShowUserRepository(currentUser: String): List<Repositories> {
        return apiServise.getShowUserRepository(currentUser)
    }


    suspend fun addFavoriteRepository(login: String, user: String, repositoryName: String) {
        try {
            val repository = Repositories(repositoryName, user, null, null, null, null)
            databaseReferenceRepository.child(login).child(repositoryName).setValue(repository)
        } catch (e: Exception) {
            Log.d("Hata", e.message.toString())
        }
    }

    suspend fun deleteFavoriteRepository(login: String, repositoryName: String) {
        try {
            databaseReferenceRepository.child(login).child(repositoryName).removeValue()
        } catch (e: Exception) {
            Log.d("Hata", e.message.toString())
        }
    }

    suspend fun getAllList(login: String) : ArrayList<Repositories> {

        return suspendCoroutine { continuation ->
            try { // Giriş yapan kullanıcının bütün favoritelerini alıyor.
                val favRepoList: ArrayList<Repositories> = ArrayList()
                var favRepo = databaseReferenceRepository.child(login)

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
                            continuation.resume(ArrayList()) // Boş liste döndürme

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
}
