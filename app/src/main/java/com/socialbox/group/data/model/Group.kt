package com.socialbox.group.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
  val id: Int? = null,
  val name: String,
  val movieList: List<GroupMovie>? = listOf(),
  val memberCount: Int,
  val photoURL: String? = "",
  val adminId: Int
) : Parcelable
