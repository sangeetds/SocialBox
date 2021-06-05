package com.socialbox.login.data

import com.socialbox.login.data.service.UserService
import com.socialbox.login.data.Result.Error
import com.socialbox.login.data.Result.Success
import com.socialbox.login.ui.login.User
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository @Inject constructor(val userService: UserService) {

  fun login(user: User) = flow {
    emit(loginUser(user))
  }

  private suspend fun loginUser(user: User): Result<User> =
    try {
      userService.loginUser(user = user).run {
        when {
          isSuccessful && body() != null -> {
            Timber.i("Login successful with response: ${raw()} ")
            Success(body()!!)
          }
          else -> {
            Timber.e("Error while logging in with error: ${errorBody()}")
            Error(Exception(errorBody().toString()))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Error while logging in with error: $exception")
      Error(Exception("Server Down. Please try again."))
    }
}
