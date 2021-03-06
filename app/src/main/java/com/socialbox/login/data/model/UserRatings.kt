package com.socialbox.login.data.model

import android.os.Parcelable
import com.socialbox.group.data.model.Movie
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserRatings(
  val id: Int? = null,
  val ratings: Double,
  val userId: User,
  val movieId: Movie,
) : Parcelable