package com.socialbox.movie.data.dto

data class UserMovieDTO(
  val id: Int,
  val userId: String,
  val name: String,
  val photoURL: String,
  val rating: Double,
  val userRating: Double,
  val votes: Int,
)
