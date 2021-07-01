package com.socialbox.movie.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.group.data.model.Movie
import com.socialbox.login.data.model.User

class MovieViewAdapter(val context: Context, val user: User?, val movieViewModel: MovieViewModel) :
  RecyclerView.Adapter<MovieViewAdapter.RecyclerViewHolder>() {

  val views = listOf<List<Movie>>()
  private val movieListAdapter by lazy { MovieDisplayAdapter() }
  private val searchMovieListAdapter by lazy { MovieListAdapter(context = context, updateCount = null) }

  class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val browseChip = view.findViewById<Chip>(R.id.browseMovies)
    val viewTitle = view.findViewById<MaterialTextView>(R.id.genreTextView)
    val moviesRecyclerView = view.findViewById<RecyclerView>(R.id.moviesRecycler)
    val searchMoviesRecyclerView = view.findViewById<RecyclerView>(R.id.searchMoviesRecyclerView)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): RecyclerViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.movie_genre_list_layout, parent, false)
    return RecyclerViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: RecyclerViewHolder,
    position: Int
  ) {
    val view = views[position]

    holder.moviesRecyclerView.adapter = movieListAdapter
    holder.moviesRecyclerView.layoutManager =
      LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
    holder.moviesRecyclerView.setHasFixedSize(true)
    setUpObservers()

    holder.searchMoviesRecyclerView.adapter = searchMovieListAdapter
    holder.searchMoviesRecyclerView.layoutManager = LinearLayoutManager(context)
  }

  override fun getItemCount() = views.size

  private fun setUpObservers() {
    user?.id?.let { movieViewModel.getUserMovies(it) }
    movieViewModel.latestMovies.observe(context as LifecycleOwner, Observer {
      val movies = it ?: return@Observer
      movieListAdapter.submitList(movies)
    })
  }
}
