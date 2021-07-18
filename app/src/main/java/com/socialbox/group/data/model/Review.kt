package com.socialbox.group.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Review (
  private val id: Int? = null,
  private val userReviews: String? = null,
  private val groupMovieId: Int? = null,
  private val movieId: Int? = null,
) : Parcelable
