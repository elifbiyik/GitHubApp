package com.ex.github.Api

import com.ex.github.Repositories
import com.ex.github.User
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiServise {


    @GET(CONSTANT.ALL_USERS)
    suspend fun getAllUser(): List<User>

    @GET(CONSTANT.ALL_REPOSITORIES)
    suspend fun getAllRepositories(): List<Repositories>

    @GET("${CONSTANT.ALL_USERS}/{currentUser}")
    suspend fun getShowUser(
        @Path("currentUser") username: String
    ): User

    @GET("${CONSTANT.ALL_USERS}/{currentUser}/followers")
    suspend fun getShowUserFollowers(
        @Path("currentUser") name: String
    ): List<User>

    @GET("${CONSTANT.ALL_USERS}/{currentUser}/following")
    suspend fun getShowUserFollowing(
        @Path("currentUser") name: String
    ): List<User>


    @GET("${CONSTANT.ALL_USERS}/{currentUser}/repos")
    suspend fun getShowUserRepository(
        @Path("currentUser") name: String
    ): List<Repositories>

    @GET("${CONSTANT.ALL_USERS}/{currentUser}/gists")
    suspend fun getShowUserGists(
        @Path("currentUser") name: String
    ): List<User>



}