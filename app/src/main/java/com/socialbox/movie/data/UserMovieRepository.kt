package com.socialbox.movie.data

import com.socialbox.login.data.service.UserService
import com.socialbox.common.util.RepositoryUtils.Companion.stringSuspending
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserMovieRepository @Inject constructor(private val userService: UserService) {

  suspend fun getUserMovies(id: Int?) =
    try {
      userService.getUserMovies(id = id!!).run {
        if (isSuccessful && body() != null) {
          Timber.i("Successfully fetched movies.")
          body()!!
        }
        else {
          Timber.d("Found no movies with error: ${errorBody()?.stringSuspending()}")
          listOf()
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Failed to connect to the server with error $exception")
      listOf()
    }
}
