package com.socialbox.group.data

import com.socialbox.common.enums.Result
import com.socialbox.common.enums.Result.Error
import com.socialbox.common.enums.Result.Success
import com.socialbox.group.data.model.Movie
import com.socialbox.group.data.service.MovieService
import com.socialbox.common.util.RepositoryUtils.Companion.stringSuspending
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieService: MovieService) {

  suspend fun getMoviesForGroup(groupId: String) =
    try {
      movieService.getMoviesForGroup(groupId).run {
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

  suspend fun searchMovie(searchQuery: String) = try {
    Timber.i("Making query for $searchQuery")
    movieService.searchMovie(searchQuery).run {
      when {
        isSuccessful && body() != null -> {
          Success(body()!!)
        }
        else -> {
          val errorMessage = "Failed to fetch movies with error: ${errorBody()?.stringSuspending()}"
          Timber.d(errorMessage)
          Error(Exception(errorMessage))
        }
      }
    }
  } catch (exception: SocketTimeoutException) {
    val errorMessage = "Failed to connect with error $exception"
    Timber.e(errorMessage)
    Error(Exception(errorMessage))
  }
}
