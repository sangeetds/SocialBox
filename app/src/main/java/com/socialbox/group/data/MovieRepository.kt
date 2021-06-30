package com.socialbox.group.data

import com.socialbox.group.data.model.Movie
import com.socialbox.group.data.service.MovieService
import com.socialbox.util.RepositoryUtils.Companion.stringSuspending
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieService: MovieService) {

  suspend fun getMovies(groupId: String) =
    try {
      movieService.getMovies(groupId).run {
        when {
          isSuccessful && body() != null -> body()!!
          else -> {
            Timber.d("Failed to fetch movies with error: ${errorBody()?.stringSuspending()}")
            listOf()
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Failed to fetch movies with error: $exception")
      listOf()
    }

  suspend fun getAllMovies() =
    try {
      movieService.getAllMovies().run {
        when {
          isSuccessful && body() != null -> body()!!
          else -> {
            Timber.d("Failed to fetch movies with error: ${errorBody()?.stringSuspending()}")
            listOf()
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Failed to connect with error $exception")
      listOf()
    }

  suspend fun updateRatings(movie: Movie) =
    try {
      movieService.saveMovie(movie)
    } catch (exception: Exception) {
      Timber.e("Failed to save movie with error $exception")
    }
}
