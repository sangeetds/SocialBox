package com.socialbox.login.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
  val id: String? = null,
  val name: String? = null,
  val password: String? = null,
  val personalMovieList: List<String>? = listOf(),
  val sharedMovieList: List<String>? = listOf(),
  val groupsId: Set<String>? = setOf(),
  val photoURL: String? = null,
  val email: String? = null,
) : Parcelable
