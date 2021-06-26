package com.socialbox.login.ui.login

import com.socialbox.login.data.model.User

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
  val success: User? = null,
  val error: String? = null
)