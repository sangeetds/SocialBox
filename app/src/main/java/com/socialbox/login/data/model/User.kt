package com.socialbox.login.data.model

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.HashSet

@Parcelize
data class User(
  @Json(name = "userId") val id: String? = null,
  val userName: String? = null,
  val userPassword: String? = null,
  val personalMovieList: List<String> = listOf(),
  val sharedMovieList: List<String> = listOf(),
  val groupsId: Set<String> = setOf(),
  val userPhotoURL: String? = null,
  val userEmail: String? = null,
) : Parcelable
