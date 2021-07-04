package com.socialbox.group.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
  val id: String? = null,
  val name: String,
  val movieList: List<GroupMovie>? = listOf(),
  val usersId: Set<String>? = setOf(),
  val memberCount: Int,
  val photoURL: String? = "",
  val adminId: String,
) : Parcelable
