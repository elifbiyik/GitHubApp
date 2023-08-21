package com.ex.github.Repository

import com.ex.github.Api.ApiServise
import com.ex.github.User
import javax.inject.Inject

class PageGistsRepository @Inject constructor(var apiServise: ApiServise) {

    suspend fun getShowUserGists(currentUser : String): List<User> {
        return apiServise.getShowUserGists (currentUser)
    }
}