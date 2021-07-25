package com.socialbox.login.data.service

import com.socialbox.group.data.model.Group
import com.socialbox.login.data.model.User
import com.socialbox.movie.data.model.UserMovie
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

  @POST("/user")
  suspend fun loginUser(@Body user: User): Response<User>

  @GET("/user/{id}/movies")
  suspend fun getUserMovies(@Path("id") id: Int): Response<List<UserMovie>>

  @POST("/user/{id}/settings")
  suspend fun updateSettings(@Body user: User, @Path("id") id: Int): Response<User>

  @GET("/user/{id}/groups")
  suspend fun getGroupsForUser(@Path("id") userId: Int): Response<List<Group>>
}