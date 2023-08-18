package com.ex.github.Repository

import com.ex.github.Api.ApiServise
import com.ex.github.User
import javax.inject.Inject

class DetailRepository @Inject constructor(var apiServise: ApiServise) {


    suspend fun getShowUser(currentUser: String): User {
        return apiServise.getShowUser(currentUser)
    }



}