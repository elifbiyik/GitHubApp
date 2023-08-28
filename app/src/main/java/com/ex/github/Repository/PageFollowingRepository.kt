package com.ex.github.Repository

import com.ex.github.Api.ApiServise
import com.ex.github.User
import javax.inject.Inject

class PageFollowingRepository @Inject constructor(var apiServise: ApiServise) {

    suspend fun getShowUserFollowing(clickedUserLogin : String): List<User> {
       return apiServise.getShowUserFollowing(clickedUserLogin)
    }
}