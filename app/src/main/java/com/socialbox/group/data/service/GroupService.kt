package com.socialbox.group.data.service

import com.socialbox.chat.data.model.Invite
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.login.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface GroupService {

  @GET("/group")
  suspend fun getGroupsForUser(@Query("ids") groupIds: List<Int>): Response<List<Group>>

  @POST("/group")
  suspend fun saveGroup(@Body group: Group): Response<Group>

  @GET("/group/{id}")
  suspend fun getGroup(@Path("id") groupId: Int): Response<Group>

  @POST("/group/movie")
  suspend fun saveGroupMovies(@Body groupMovies: List<GroupMovie>): Response<List<GroupMovie>>

  @GET("/group/invite")
  suspend fun getInviteLink(
    @Query("groupId") groupId: Int,
    @Query("userId") userId: Int
  ): Response<Invite>

  @GET("/group/{id}")
  suspend fun addUserToGroup(
    @Path("id") groupId: Int,
    @Query("userId") userId: Int?
  ): Response<Group>

  @GET("/group/{id}/users")
  suspend fun getUsers(@Path("id") groupId: Int): Response<List<User>>
}