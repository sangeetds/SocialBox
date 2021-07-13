package com.socialbox.group.data.model

import android.os.Parcelable
import com.socialbox.login.data.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
  val id: Int? = null,
  val name: String,
  val movieList: List<GroupMovie>? = listOf(),
  val usersId: List<User>? = listOf(),
  val memberCount: Int,
  val photoURL: String? = "",
  val admin: User,
) : Parcelable
