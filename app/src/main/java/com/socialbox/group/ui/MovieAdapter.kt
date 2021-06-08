package com.socialbox.group.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.group.data.model.GroupMovie

class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

  var cardList = listOf<GroupMovie>()

  class ViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(com.socialbox.R.id.movie_image)
    val name: TextView = cardView.findViewById(com.socialbox.R.id.movie_name)
    val ratings: TextView = cardView.findViewById(com.socialbox.R.id.movie_rating)
    val addedBy: TextView = cardView.findViewById(com.socialbox.R.id.movie_added_by)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.moview_list_layout, parent, false)

    return ViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: ViewHolder,
    position: Int
  ) {
    val movie = cardList[position]

    holder.name.text = movie.name
    holder.ratings.text = "${movie.rating}/5.0"
    holder.addedBy.text = "Added by: ${movie.createdBy}"
  }

  override fun getItemCount() = cardList.size
}
