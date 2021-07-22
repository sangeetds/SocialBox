package com.socialbox.group.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class Group(
  val id: Int? = null,
  val name: String?,
  @Json(name = "groupMovieDTOList") val movieList: List<GroupMovie>? = listOf(),
  val memberCount: Int,
  val photoURL: String? = "",
  val adminId: Int?
) : Parcelable
