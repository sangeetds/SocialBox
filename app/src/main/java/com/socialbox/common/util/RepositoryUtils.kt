package com.socialbox.common.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody

class RepositoryUtils {

  companion object {

    @Suppress("BlockingMethodInNonBlockingContext")
    suspend fun ResponseBody.stringSuspending() =
      withContext(Dispatchers.IO) { string() }
  }
}