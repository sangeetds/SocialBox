package com.socialbox.login.data.model

import android.net.Uri
import android.os.Parcelable
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.Movie
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
  val id: Int? = null,
  val name: String? = null,
  val personalMovieList: List<UserRatings>? = listOf(),
  val sharedMovieList: List<Movie>? = listOf(),
  val groups: MutableList<Group> = mutableListOf(),
  val owningGroup: MutableList<Group> = mutableListOf(),
  @Transient val photoURL: Uri? = Uri.EMPTY,
  val email: String? = null,
) : Parcelable
