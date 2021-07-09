package com.socialbox.movie.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.common.enums.Genre.PERSONAL
import com.socialbox.group.data.model.Movie
import com.socialbox.login.data.model.User
import java.util.Locale

class MovieViewAdapter(
  val context: Context,
  val user: User?,
  private val movieViewModel: MovieViewModel,
  private val setSearchMode: () -> Unit

) :
  RecyclerView.Adapter<MovieViewAdapter.RecyclerViewHolder>() {

  val views = listOf<List<Movie>>()
  val genres = listOf(PERSONAL)
  private val movieListAdapter by lazy { MovieDisplayAdapter(context, movieViewModel) }

  class RecyclerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val browseChip: Chip = view.findViewById(R.id.browseMovies)
    val viewTitle: MaterialTextView = view.findViewById(R.id.genreTextView)
    val moviesRecyclerView: RecyclerView = view.findViewById(R.id.moviesRecycler)
  }

  override fun onCreateViewHolder(
    parent: ViewGroup,
    viewType: Int
  ): RecyclerViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.card_movie_genre, parent, false)

    return RecyclerViewHolder(view)
  }

  override fun onBindViewHolder(
    holder: RecyclerViewHolder,
    position: Int
  ) {
    val view = views[position]
    holder.viewTitle.text = holder.viewTitle.text.toString().format(
      genres[position].name.lowercase(Locale.getDefault()).replaceFirstChar { it.uppercase() })

    val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true)
    linearLayoutManager.reverseLayout = false
    holder.moviesRecyclerView.adapter = movieListAdapter
    holder.moviesRecyclerView.layoutManager = linearLayoutManager
    holder.moviesRecyclerView.setHasFixedSize(true)
    movieListAdapter.submitList(view)

    holder.browseChip.setOnClickListener {
      setSearchMode()
    }
  }

  override fun getItemCount() = views.size
}
