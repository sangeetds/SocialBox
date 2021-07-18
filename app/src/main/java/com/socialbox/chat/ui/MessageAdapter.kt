package com.socialbox.chat.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.chat.data.model.Message

class MessageAdapter(val id: Int?, val photoUri: String?) : ListAdapter<Message, MessageAdapter.ViewHolder>(MessageItemDiffCallback()) {

  class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val otherUserAvatar: ImageView? = view.findViewById(R.id.imageChatProfileOther)
    val otherUserName: TextView? = view.findViewById(R.id.textChatUserOther)
    val otherUserMessage: TextView? = view.findViewById(R.id.textChatMessageOther)
    val otherTimeStamp: TextView? = view.findViewById(R.id.textChatTimestampOther)
    val otherUserLayout: ConstraintLayout? = view.findViewById(R.id.otherUserLayout)

    val selfUserMessage: TextView? = view.findViewById(R.id.textChatMessageSelf)
    val selfTimeStamp: TextView? = view.findViewById(R.id.textChatTimestampSelf)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val inflater = LayoutInflater.from(parent.context)
    return when(viewType) {
      1 -> {
        ViewHolder(inflater.inflate(R.layout.chat_bubble_self, parent, false))
      }
      else -> {
        ViewHolder(inflater.inflate(R.layout.chat_bubble_other, parent, false))
      }
    }
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val message = getItem(position)

    when(getItemViewType(position)) {
      1 -> {
        holder.selfTimeStamp?.text = message.createdAt
         holder.selfUserMessage?.text = message.content
      }
      else -> {
        holder.otherTimeStamp?.text = message.createdAt
        holder.otherUserMessage?.text = message.content
        if (position == 0 || getItemViewType(position - 1) != 0) {
          holder.otherUserName?.text = message.senderName
        }
        else {
          holder.otherUserName?.visibility = View.GONE
          holder.otherUserAvatar?.visibility = View.INVISIBLE
        }
      }
    }
  }

  override fun getItemViewType(position: Int) = if (getItem(position).senderId == id) 1 else 0
}

class MessageItemDiffCallback : DiffUtil.ItemCallback<Message>() {

  override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem == newItem

  override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean = oldItem == newItem
}
