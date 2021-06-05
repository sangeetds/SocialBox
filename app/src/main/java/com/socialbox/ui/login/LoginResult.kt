package com.socialbox.ui.login

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
  val success: User? = null,
  val error: Int? = null
)