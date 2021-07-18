package com.socialbox.group.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupMovie(
  val id: Int? = null,
  val groupId: Int?,
  val name: String = "",
  val photoURL: String? = "",
  val rating: Double = 0.0,
  val votes: Int = 0,
  val reviews: List<Review> = listOf()
) : Parcelable
