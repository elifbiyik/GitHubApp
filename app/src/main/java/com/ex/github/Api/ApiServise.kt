package com.ex.github.Api

import com.ex.github.Repositories
import com.ex.github.User
import okhttp3.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServise {


    @GET(CONSTANT.ALL_USERS)
    suspend fun getAllUser(): List<User>


    @GET(CONSTANT.ALL_REPOSITORIES)
    suspend fun getAllRepositories(): List<Repositories>

    @GET("${CONSTANT.ALL_USERS}/{clickedUserLogin}")
    suspend fun getShowUser(
        @Path("clickedUserLogin") username: String
    ): User

    @GET("${CONSTANT.ALL_USERS}/{clickedUserLogin}/followers")
    suspend fun getShowUserFollowers(
        @Path("clickedUserLogin") name: String
    ): List<User>

    @GET("${CONSTANT.ALL_USERS}/{clickedUserLogin}/following")
    suspend fun getShowUserFollowing(
        @Path("clickedUserLogin") name: String
    ): List<User>


    @GET("${CONSTANT.ALL_USERS}/{clickedUserLogin}/repos")
    suspend fun getShowUserRepository(
        @Path("clickedUserLogin") name: String
    ): List<Repositories>




}