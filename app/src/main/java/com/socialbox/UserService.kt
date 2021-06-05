package com.socialbox

import com.socialbox.ui.login.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {

  @POST("/user")
  suspend fun loginUser(@Body user: User): Response<User>
}