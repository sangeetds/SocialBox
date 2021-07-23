package com.socialbox.group.ui

import com.socialbox.group.data.model.Group

data class GroupResult(
  val success: List<Group>? = null,
  val created: Group? = null,
  val error: String? = null
)