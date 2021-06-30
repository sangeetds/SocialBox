package com.socialbox.group.data.service

import com.socialbox.group.data.model.Movie
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MovieService {

  @GET("/movie")
  suspend fun getMovies(@Query("groupID") groupId: String): Response<List<Movie>>

  @GET("/movie")
  suspend fun getAllMovies(): Response<List<Movie>>

  @POST("/movie")
  suspend fun saveMovie(movie: Movie): Response<Movie>
}
