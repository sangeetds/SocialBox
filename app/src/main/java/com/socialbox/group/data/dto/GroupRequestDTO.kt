package com.socialbox.group.data.dto

import com.socialbox.group.data.model.GroupMovie
import com.socialbox.login.data.model.User

data class GroupRequestDTO(
  val id: Int? = null,
  val name: String,
  val movieList: List<GroupMovie>? = listOf(),
  val users: List<User>? = listOf(),
  val memberCount: Int,
  val photoURL: String? = "",
  val admin: User?
)
