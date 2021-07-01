package com.socialbox.group.data.model

data class Movie(
  val id: String?,
  val name: String = "",
  val rating: Double = 0.0,
  val reviews: List<String>?,
  val photoURL: String?
)
