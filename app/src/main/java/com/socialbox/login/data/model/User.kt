package com.socialbox.login.data.model

import android.os.Parcelable
import com.socialbox.group.data.model.Group
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
  val id: Int? = null,
  val name: String? = null,
  val displayName: String? = null,
  val personalMovieList: List<UserRatings>? = listOf(),
  val groups: MutableList<Group> = mutableListOf(),
  val photoURL: String? = "",
  val email: String? = null,
) : Parcelable
