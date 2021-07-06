package com.socialbox.group.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupMovie(
  val id: String? = null,
  val groupId: String,
  val name: String = "",
  val photoURL: String? = "",
  val rating: Double = 0.0,
  val votes: Int = 0,
) : Parcelable
