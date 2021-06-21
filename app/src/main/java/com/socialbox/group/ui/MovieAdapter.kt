package com.socialbox.group.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.group.data.model.Movie

// Todo: Make it a list of Group Movie
class MovieAdapter : RecyclerView.Adapter<MovieAdapter.ViewHolder>() {

  var movies = listOf<Movie>()

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
    val movie = movies[position]
    holder.name.text = movie.name
    holder.ratings.text = "${movie.rating}/5.0"
  }

  override fun getItemCount() = movies.size
}

class MovieItemDiffCallback : DiffUtil.ItemCallback<Movie>() {

  override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem

  override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean = oldItem == newItem
}
