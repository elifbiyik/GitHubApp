package com.ex.github.Repository

import android.annotation.SuppressLint
import com.ex.github.Repositories
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.lang.Exception
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class FavoriteRepositoryRepository @Inject constructor(var database: FirebaseDatabase ) {

    private val databaseReferenceRepository =
        database.getReference("Repository")


    suspend fun getAllList(loginUser: String): ArrayList<Repositories> {

        return suspendCoroutine { continuation ->
            try { // Giriş yapan kullanıcının bütün favoritelerini alıyor.
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