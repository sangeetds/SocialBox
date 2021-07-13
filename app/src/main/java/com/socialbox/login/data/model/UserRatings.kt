package com.socialbox.login.data.model

import android.os.Parcelable
import com.socialbox.group.data.model.Movie
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRatings(
  val id: Int,
  val ratings: Double,
  val user: User,
  val movie: Movie,
) : Parcelable