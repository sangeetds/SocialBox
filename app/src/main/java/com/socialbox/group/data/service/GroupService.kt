package com.socialbox.group.data.service

import com.socialbox.group.data.dto.GroupDTO
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GroupService {

  @GET("/groups")
  fun getGroupsForUser(@Query("userId") userId: String): Response<GroupDTO>
}