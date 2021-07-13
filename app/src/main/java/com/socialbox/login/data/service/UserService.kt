package com.socialbox.login.data.service

import com.socialbox.login.data.model.User
import com.socialbox.movie.data.dto.UserMovieDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserService {

  @POST("/user")
  suspend fun loginUser(@Body user: User): Response<User>

  @GET("/user/{id}/movies")
  suspend fun getUserMovies(@Path("id") id: Int): Response<List<UserMovieDTO>>
}