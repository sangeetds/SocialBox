package com.socialbox.group.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.login.data.model.User
import com.socialbox.group.ui.GroupAdapter.SongSearchViewHolder
import com.squareup.picasso.Picasso

class GroupAdapter(
  val context: Context,
  val user: User?
) :
  RecyclerView.Adapter<SongSearchViewHolder>() {

  var groupList = mutableListOf<GroupDTO>()

  class SongSearchViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(R.id.shapeableImageView)
    val groupName: TextView = cardView.findViewById(R.id.group_name)
    val memberCount: TextView = cardView.findViewById(R.id.members)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): SongSearchViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.group_card_layout, parent, false)

    return SongSearchViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: SongSearchViewHolder,
    position: Int
  ) {
    val group = groupList[position]

    holder.itemView.setOnClickListener {

    }

    holder.groupName.text = group.groupName
    holder.memberCount.text = String.format(holder.memberCount.text.toString(), group.memberCount)
    Picasso.get().load(group.groupPhotoURL).into(holder.image)
  }

  override fun getItemCount(): Int = this.groupList.size
}