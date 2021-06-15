package com.socialbox.login.data

import com.socialbox.login.data.Result.Error
import com.socialbox.login.data.Result.Success
import com.socialbox.login.data.model.User
import com.socialbox.login.data.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Class that requests authentication and user information from the remote data source.
 */
class LoginRepository @Inject constructor(private val userService: UserService) {

  suspend fun login(user: User) =
    try {
      userService.loginUser(user = user).run {
        when {
          isSuccessful && body() != null -> {
            Timber.i("Login successful with response: ${raw()} ")
            Success(body()!!)
          }
          else -> {
            Timber.e("Error while logging in with error: ${errorBody()}")
            Error(Exception(errorBody()?.stringSuspending()))
          }
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e("Error while logging in with error: $exception")
      Error(Exception("Server Down. Please try again."))
    }

  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun ResponseBody.stringSuspending() =
    withContext(Dispatchers.IO) { string() }
}
