package com.socialbox.group.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
  val groupId: String = "",
  val groupName: String,
  val groupMovieList: List<GroupMovie> = listOf(),
  val usersId: Set<String> = setOf(),
  val memberCount: Int,
  val groupPhotoURL: String = "",
  val groupAdminId: String,
) : Parcelable
