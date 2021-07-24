package com.socialbox.group.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.chat.ui.ChatActivity
import com.socialbox.group.data.model.Group
import com.socialbox.group.ui.GroupAdapter.SongSearchViewHolder
import com.socialbox.login.data.model.User
import timber.log.Timber

class GroupAdapter(val context: Context, val user: User?) :
  ListAdapter<Group, SongSearchViewHolder>(GroupItemDiffCallback()) {

  class SongSearchViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val groupName: TextView = cardView.findViewById(R.id.group_name)
    val memberCount: TextView = cardView.findViewById(R.id.members)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongSearchViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.card_group, parent, false)

    return SongSearchViewHolder(view)
  }

  override fun onBindViewHolder(holder: SongSearchViewHolder, position: Int) {
    val group = getItem(position)

    holder.itemView.setOnClickListener {
      Timber.i("Opening group $group")
      val intent = GroupDetailsActivity.createIntent(context, group, user)
      context.startActivity(intent)
    }

    holder.groupName.text = group.name
    holder.memberCount.text = holder.memberCount.text.toString().format(group.memberCount)
  }
}

class GroupItemDiffCallback : DiffUtil.ItemCallback<Group>() {

  override fun areItemsTheSame(oldItem: Group, newItem: Group): Boolean = oldItem == newItem

  override fun areContentsTheSame(oldItem: Group, newItem: Group): Boolean = oldItem == newItem
}