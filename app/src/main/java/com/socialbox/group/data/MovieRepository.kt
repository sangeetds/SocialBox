package com.socialbox.group.data

import com.socialbox.group.data.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieService: MovieService) {

  fun getMovies(groupId: String): Flow<List<Movie>> = flow {
    emit(fetchMovies(groupId))
  }

  private suspend fun fetchMovies(groupId: String): List<Movie> =
    try {
      movieService.getMovies(groupId).run {
        body()!!
      }
    } catch (exception: SocketTimeoutException) {
      Timber.i("Failed to fetch movies")
      listOf()
    }

  fun getAllMovies() = flow {
    emit(fetchAllMovies())
  }

  private suspend fun fetchAllMovies() =
    try {
      movieService.getAllMovies().run {
        body()!!
      }
    } catch (exception: SocketTimeoutException) {
      Timber.i("Failed to fetch movies")
      listOf()
    }
}
