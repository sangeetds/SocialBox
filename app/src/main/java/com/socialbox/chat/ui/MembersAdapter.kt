package com.socialbox.chat.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.socialbox.R.id
import com.socialbox.R.layout
import com.socialbox.login.data.model.User
import de.hdodenhof.circleimageview.CircleImageView

class MembersAdapter(var members: List<User>) : RecyclerView.Adapter<MembersAdapter.Holder>() {

  class Holder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val card: MaterialCardView = cardView.findViewById(id.searchMovieCard)
    val image: CircleImageView = cardView.findViewById(id.userMovieImage)
    val movieName: TextView = cardView.findViewById(id.movieName)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int,
  ): Holder {
    val itemView: View = LayoutInflater.from(parent.context)
      .inflate(layout.card_member_details, parent, false)

    return Holder(itemView)
  }

  override fun onBindViewHolder(
    holder: Holder,
    position: Int,
  ) {
    val members = members[position]
    holder.movieName.text = members.name
  }

  override fun getItemCount() = members.size
}
