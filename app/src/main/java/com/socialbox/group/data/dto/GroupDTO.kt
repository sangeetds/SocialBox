package com.socialbox.group.data.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GroupDTO(
  val id: String = "",
  val name: String = "",
  val photoURL: String? = "",
  val memberCount: Int = 0
) : Parcelable
