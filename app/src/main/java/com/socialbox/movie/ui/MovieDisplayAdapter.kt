package com.socialbox.movie.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DiffUtil.ItemCallback
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.group.data.model.Movie

class MovieDisplayAdapter(
  val context: Context,
  private val movieViewModel: MovieViewModel,
) : ListAdapter<Movie, MovieDisplayAdapter.MovieHolder>(MovieDiffCallback()) {

  class MovieHolder(view: View) : RecyclerView.ViewHolder(view) {
    val image: ImageView = view.findViewById(R.id.movieCardImage)
    val ratings: MaterialTextView = view.findViewById(R.id.movieStarRatings)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ):MovieHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.card_movie, parent, false)
    return MovieHolder(view)
  }

  override fun onBindViewHolder(holder: MovieHolder, position: Int) {
    val movie = getItem(position)
    holder.ratings.text = String.format(holder.ratings.text.toString(), movie.rating)
  }

  private fun setUpObservers() {

  }
}

class MovieDiffCallback : ItemCallback<Movie>() {

  override fun areItemsTheSame(
    oldItem: Movie,
    newItem: Movie,
  ): Boolean = oldItem == newItem

  override fun areContentsTheSame(
    oldItem: Movie,
    newItem: Movie,
  ): Boolean = oldItem == newItem
}
