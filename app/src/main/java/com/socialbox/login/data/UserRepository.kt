package com.socialbox.login.data

import com.socialbox.Result.Error
import com.socialbox.Result.Success
import com.socialbox.login.data.model.User
import com.socialbox.login.data.service.UserService
import com.socialbox.util.RepositoryUtils.Companion.stringSuspending
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source.
 */
class UserRepository @Inject constructor(private val userService: UserService) {

  suspend fun login(user: User) =
    try {
      userService.loginUser(user = user).run {
        when {
          isSuccessful && body() != null -> {
            Timber.i("Login successful with response: ${raw()} ")
            Success(body()!!)
          }
          else -> {
            Timber.e("Error while logging in with error: ${errorBody()?.stringSuspending()}")
            Error(Exception(errorBody()?.stringSuspending()))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Error while logging in with error: $exception")
      Error(Exception("Server Down. Please try again."))
    }

  suspend fun getAllMovies(userId: String) =
    try {
      userService.getUserMovies(userId).run {
        when {
          isSuccessful && body() != null -> {
            Timber.i("Fetched movies successful with response: ${raw()} ")
            Success(body()!!)
          }
          else -> {
            Timber.e("Error while fetch movies for the user with error: ${errorBody()?.stringSuspending()}")
            Error(Exception(errorBody()?.stringSuspending()))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Error while fetching movies with error: $exception")
      Error(Exception("Server Down. Please try again."))
    }
}
