package com.socialbox.movie.data.model

data class UserMovie(
  val id: Int,
  val userId: String,
  val name: String,
  val photoURL: String,
  val rating: Double,
  val userRating: Double,
  val votes: Int,
)
