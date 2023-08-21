package com.ex.github.Repository

import com.ex.github.Api.ApiServise
import com.ex.github.Repositories
import com.ex.github.User
import javax.inject.Inject

class PageRepositoryRepository @Inject constructor(var apiServise: ApiServise) {

    suspend fun getShowUserRepository(currentUser : String): List<Repositories> {
        return apiServise.getShowUserRepository (currentUser)
    }
}