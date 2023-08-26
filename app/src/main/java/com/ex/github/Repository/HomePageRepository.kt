package com.ex.github.Repository

import com.ex.github.Api.ApiServise
import com.ex.github.Repositories
import com.ex.github.User
import javax.inject.Inject

class HomePageRepository @Inject constructor(var apiServise: ApiServise) {
    suspend fun getAllUsers (): List<User> {
        return apiServise.getAllUser()
    }
    suspend fun getAllRepositories (): List<Repositories> {
        return apiServise.getAllRepositories()
    }
}