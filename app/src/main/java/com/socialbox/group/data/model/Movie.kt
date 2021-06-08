package com.socialbox.group.data.model

data class Movie(
  private val movieId: String,
  private val movieName: String,
  private val movieRating: Double = 0.0,
  private val movieReviews: List<String>,
  private val moviePhotoURL: String
)
