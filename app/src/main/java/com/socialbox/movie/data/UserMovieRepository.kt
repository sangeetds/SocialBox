package com.socialbox.movie.data

import com.socialbox.movie.data.service.UserMovieService
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserMovieRepository @Inject constructor(private val userMovieService: UserMovieService) {

  suspend fun getUserMovies(id: String) =
    try {
      userMovieService.getUserMovies(id = id).run {
        if (isSuccessful && body() != null) {
          body()!!
        }
        else {
          listOf()
        }
      }
    } catch (exception: SocketTimeoutException) {
      listOf()
    }
}
