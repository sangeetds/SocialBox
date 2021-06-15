package com.socialbox.group.data

import com.socialbox.group.data.service.MovieService
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieService: MovieService) {

  suspend fun getMovies(groupId: String) =
    try {
      movieService.getMovies(groupId).run {
        body()!!
      }
    } catch (exception: SocketTimeoutException) {
      Timber.i("Failed to fetch movies")
      listOf()
    }

  suspend fun getAllMovies() =
    try {
      movieService.getAllMovies().run {
        body()!!
      }
    } catch (exception: SocketTimeoutException) {
      Timber.i("Failed to fetch movies")
      listOf()
    }
}
