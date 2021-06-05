package com.socialbox.ui.login

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize
import java.util.HashSet

@Parcelize
data class User(
  @Json(name = "userId") private val id: String? = null,
  private val userName: String? = null,
  private val personalMovieList: List<String> = listOf(),
  private val sharedMovieList: List<String> = listOf(),
  private val groupsId: HashSet<String> = hashSetOf(),
  private val userPhotoURL: String? = null,
  private val userEmail: String? = null,
) : Parcelable
