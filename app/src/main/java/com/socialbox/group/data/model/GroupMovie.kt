package com.socialbox.group.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupMovie(
  val id: String? = null,
  val groupId: String,
  val name: String = "",
  val photoUrl: String = "",
  val rating: Double = 0.0,
  val votes: Int = 0,
) : Parcelable
