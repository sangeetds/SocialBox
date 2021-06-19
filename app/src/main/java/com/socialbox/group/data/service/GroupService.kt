package com.socialbox.group.data.service

import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.GroupMovie
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {

  @GET("/group")
  suspend fun getGroupsForUser(@Query("ids") groupIds: List<String>): Response<List<GroupDTO>>

  @POST("/group")
  suspend fun saveGroup(@Body group: Group): Response<Group>

  @GET("/group/{id}")
  suspend fun getGroup(@Path("id") groupId: String): Response<Group>

  @GET("/group/movie")
  suspend fun saveGroupMovies(@Body groupMovies: List<GroupMovie>): Response<GroupMovie>
}