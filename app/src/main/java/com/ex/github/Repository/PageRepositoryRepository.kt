package com.ex.github.Repository

import android.util.Log
import com.ex.github.Api.ApiServise
import com.ex.github.Repositories
import com.google.firebase.database.FirebaseDatabase
import java.lang.Exception
import javax.inject.Inject

class PageRepositoryRepository @Inject constructor(
    var apiServise: ApiServise,
    var database: FirebaseDatabase
) {

    private val databaseReferenceRepository =
        database.getReference("Repository")


    suspend fun getShowUserRepository(currentUser: String): List<Repositories> {
        return apiServise.getShowUserRepository(currentUser)
    }



    suspend fun addFavoriteRepository(login: String, repositoryName : String) {
        try {
            val repository = Repositories(repositoryName, null, null, null, null )
            databaseReferenceRepository.child(login).setValue(repository)
        } catch (e: Exception) {
            Log.d("Hata", e.message.toString())
        }
    }


}