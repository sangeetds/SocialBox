package com.socialbox.common.enums

/**
 * Authentication result : success (user details) or error message.
 */
data class ResultData<T : Any>(
  var success: T? = null,
  val created: T? = null,
  val error: String? = null
)