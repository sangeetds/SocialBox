package com.socialbox.movie.data.service

import com.socialbox.movie.data.dto.UserMovieDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface UserMovieService {

  @GET("/user/{id}/movies")
  suspend fun getUserMovies(@Path("id") id: String): Response<List<UserMovieDTO>>
}
