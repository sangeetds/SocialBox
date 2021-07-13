package com.socialbox.chat.data.model

data class Message(
  val id: String? = null,
  val senderId: Int = 0,
  val senderName: String = "",
  val content: String = "",
  val createdAt: String
)
