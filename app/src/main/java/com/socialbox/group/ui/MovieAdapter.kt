package com.socialbox.group.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.group.data.model.Movie

// Todo: Should it be a list of Group Movie?
class MovieAdapter : ListAdapter<GroupMovie, MovieAdapter.ViewHolder>(MovieItemDiffCallback()) {

  class ViewHolder(cardView: View) : RecyclerView.ViewHolder(cardView) {
    val image: ImageView = cardView.findViewById(R.id.movie_image)
    val name: TextView = cardView.findViewById(R.id.movie_name)
    val ratings: TextView = cardView.findViewById(R.id.movie_rating)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val layoutInflater = LayoutInflater.from(parent.context)
    val view = layoutInflater
      .inflate(R.layout.group_movie_card_layout, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val movie = getItem(position)
    holder.name.text = movie.name
    holder.ratings.text = holder.ratings.text.toString().format(movie.rating)
  }

}

class MovieItemDiffCallback : DiffUtil.ItemCallback<GroupMovie>() {

  override fun areItemsTheSame(oldItem: GroupMovie, newItem: GroupMovie): Boolean = oldItem == newItem

  override fun areContentsTheSame(oldItem: GroupMovie, newItem: GroupMovie): Boolean = oldItem == newItem
}
