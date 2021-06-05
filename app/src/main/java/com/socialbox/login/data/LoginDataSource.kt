package com.socialbox.login.data

import com.socialbox.login.data.model.LoggedInUser
import com.socialbox.login.data.Result.Error
import com.socialbox.login.data.Result.Success
import java.io.IOException
import java.util.UUID

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

  fun login(
    username: String,
    password: String
  ): Result<LoggedInUser> {
    try {
      // TODO: handle loggedInUser authentication
      val fakeUser = LoggedInUser(UUID.randomUUID().toString(), "Jane Doe")
      return Success(fakeUser)
    } catch (e: Throwable) {
      return Error(IOException("Error logging in", e))
    }
  }

  fun logout() {
    // TODO: revoke authentication
  }
}