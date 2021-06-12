package com.socialbox.group.data.model

data class Movie(
  val movieId: String,
  val movieName: String,
  val movieRating: Double = 0.0,
  val movieReviews: List<String>?,
  val moviePhotoURL: String?
)
