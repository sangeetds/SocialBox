package com.socialbox.movie.data

import com.socialbox.login.data.service.UserService
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserMovieRepository @Inject constructor(private val userService: UserService) {

  suspend fun getUserMovies(id: String) =
    try {
      userService.getUserMovies(id = id).run {
        if (isSuccessful && body() != null) {
          Timber.i("Successfully fetched movies.")
          body()!!
        }
        else {
          Timber.d("Found no movies.")
          listOf()
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Failed to connect to the server")
      listOf()
    }
}
