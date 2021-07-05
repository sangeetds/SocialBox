package com.socialbox.group.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.ui.GroupAdapter.SongSearchViewHolder
import com.socialbox.login.data.model.User
import timber.log.Timber

class GroupAdapter(val context: Context, val user: User?) :
  ListAdapter<GroupDTO, SongSearchViewHolder>(GroupItemDiffCallback()) {

  class SongSearchViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(R.id.user_movie_image)
    val groupName: TextView = cardView.findViewById(R.id.group_name)
    val memberCount: TextView = cardView.findViewById(R.id.members)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongSearchViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.group_card_layout, parent, false)

    return SongSearchViewHolder(view)
  }

  override fun onBindViewHolder(holder: SongSearchViewHolder, position: Int) {
    val group = getItem(position)

    holder.itemView.setOnClickListener {
      Timber.i("Opening groups $group")
      val intent = Intent(context, GroupDetailsActivity::class.java)
      intent.putExtra("groupDTO", group)
      intent.putExtra("user", user)
      context.startActivity(intent)
    }

    holder.groupName.text = group.name
    holder.memberCount.text = String.format(holder.memberCount.text.toString(), group.memberCount)

    // Todo: Set image later
    // Picasso.get().load(group.groupPhotoURL).into(holder.image)
  }
}

class GroupItemDiffCallback : DiffUtil.ItemCallback<GroupDTO>() {

  override fun areItemsTheSame(oldItem: GroupDTO, newItem: GroupDTO): Boolean = oldItem == newItem

  override fun areContentsTheSame(oldItem: GroupDTO, newItem: GroupDTO): Boolean = oldItem == newItem
}