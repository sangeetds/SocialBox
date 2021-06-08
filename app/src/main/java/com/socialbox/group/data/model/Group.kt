package com.socialbox.group.data.model

data class Group(
  val groupId: String = "",
  val groupName: String,
  val groupMovieList: List<String> = listOf(),
  val usersId: Set<String> = setOf(),
  val memberCount: Int,
  val groupPhotoURL: String = "",
  val groupAdminId: String,
)
