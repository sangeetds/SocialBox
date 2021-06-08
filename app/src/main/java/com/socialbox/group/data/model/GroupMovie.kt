package com.socialbox.group.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupMovie(
  val name: String = "",
  val image: String = "",
  val rating: Double = 0.0,
  val votes: Int = 0,
  val createdBy: String = ""
) : Parcelable
