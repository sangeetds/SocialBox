package com.socialbox.group.data.model

import android.os.Parcelable
import com.socialbox.common.enums.Genre
import com.socialbox.login.data.model.User
import com.socialbox.login.data.model.UserRatings
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
  val id: Int?,
  val name: String = "",
  val rating: Double = 0.0,
  val votes: Int = 0,
  val reviews: List<Review>?,
  val photoURL: String?,
  val genre: List<Genre> = listOf(),
  val user: User? = null,
  val userRatings: UserRatings? = null
) : Parcelable
