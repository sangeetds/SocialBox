package com.socialbox.login.data

import com.socialbox.common.enums.Result.Created
import com.socialbox.common.enums.Result.Error
import com.socialbox.common.enums.Result.Success
import com.socialbox.common.util.RepositoryUtils.Companion.stringSuspending
import com.socialbox.login.data.model.User
import com.socialbox.login.data.service.UserService
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
            if (code() == 200) {
              Timber.i("Login successful with response: ${raw()} ")
              Success(body()!!)
            } else {
              Timber.i("Created a new user with response: ${raw()} ")
              Created(body()!!)
            }
          }
          else -> {
            val message = errorBody()?.stringSuspending()
            Timber.e("Error while logging in with error: $message")
            Error(Exception(message))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Error while logging in with error: $exception")
      Error(Exception("Server Down. Please try again."))
    }

  suspend fun updateSettings(user: User) =
    try {
      userService.updateSettings(user = user, id = user.id!!).run {
        when {
          isSuccessful && body() != null -> {
            Timber.i("Login successful with response: ${raw()} ")
            Success(body()!!)
          }
          else -> {
            val message = errorBody()?.stringSuspending()
            Timber.e("Error while logging in with error: $message")
            Error(Exception("User Name already taken. Please pick a new one"))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Error while logging in with error: $exception")
      Error(Exception("Server Down. Please try again."))
    }

  suspend fun getMovies(id: Int) =
    try {
      userService.getUserMovies(id).run {
        when {
          isSuccessful && body() != null -> {
            Timber.i("Fetched movies successful with response: ${raw()} ")
            Success(body()!!)
          }
          else -> {
            val message = errorBody()?.stringSuspending()
            Timber.e("Error while fetching in with error: $message")
            Error(Exception(message))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Error while fetching in with error: $exception")
      Error(Exception("Server Down. Please try again."))
    }

  suspend fun getGroupsForUser(userId: Int) =
    try {
      Timber.i("Fetching groups for $userId")
      userService.getGroupsForUser(userId).run {
        if (isSuccessful && body() != null) {
          Timber.i("Successfully fetched groups from server.")
          Success(body()!!)
        } else {
          val message = errorBody()?.stringSuspending()
          Timber.e(message)
          Error(Exception(message))
        }
      }
  } catch (exception: SocketTimeoutException) {
      Timber.e("Error while fetching in with error: $exception")
      Error(Exception("Server Down. Please try again."))
    }
}
