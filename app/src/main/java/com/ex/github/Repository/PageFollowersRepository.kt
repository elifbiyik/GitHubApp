package com.ex.github.Repository

import com.ex.github.Api.ApiServise
import com.ex.github.User
import javax.inject.Inject

class PageFollowersRepository @Inject constructor(var apiServise: ApiServise) {

    suspend fun getShowUserFollowers (clickedUserLogin: String) : List<User> {
        return apiServise.getShowUserFollowers(clickedUserLogin)
    }
}