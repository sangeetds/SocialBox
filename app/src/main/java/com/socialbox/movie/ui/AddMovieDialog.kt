package com.socialbox.movie.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.R.layout
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.group.data.model.Movie
import com.socialbox.group.ui.GroupViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddMovieDialog(
  private val userMovieViewModel: UserMovieViewModel,
  private val groupViewModel: GroupViewModel,
  private val userId: Int,
  val group: Group,
  private val url: String
) : BottomSheetDialogFragment() {

  private lateinit var adapter: MovieListAdapter
  private lateinit var topHeader: MaterialTextView
  private lateinit var recyclerView: RecyclerView
  private lateinit var selectedTopHeader: MaterialTextView

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)

    return inflater.inflate(layout.dialog_add_movie, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpViews(view)
    setUpObservables()
  }

  private fun setUpObservables() {
    userMovieViewModel.userMovies.observe(this@AddMovieDialog, Observer {
      val movies = it ?: return@Observer

      adapter.movies =
        (movies.map { m -> Movie(id = m.id, name = m.name, photoURL = m.photoURL, reviews = null) })
      (recyclerView.adapter as MovieListAdapter).notifyDataSetChanged()
    })
  }

  private fun setUpViews(inflate: View) {
    val clearSelection = inflate.findViewById<ImageView>(R.id.clear)
    val addMovieButton = inflate.findViewById<MaterialButton>(R.id.addMovieButton)
    recyclerView = inflate.findViewById(R.id.movieListRecyclerView)
    val mLayoutManager: LayoutManager = LinearLayoutManager(context)
    val updateCount = updateCount(addMovieButton, clearSelection)
    topHeader = inflate.findViewById(R.id.searchMoviesText)
    selectedTopHeader = inflate.findViewById(R.id.selected_top_header)
    adapter = MovieListAdapter(requireContext(), updateCount, url = url)

    recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    recyclerView.adapter = adapter
    recyclerView.layoutManager = mLayoutManager
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.setHasFixedSize(true)

    addMovieButton.isEnabled = false
    addMovieButton.setOnClickListener {
      val groupMovies = toGroupMovies()
      groupViewModel.addMovies(groupMovies)
      dismiss()
    }

    clearSelection.setOnClickListener {
      adapter.selectedItemsList.clear()
      adapter.selectedItems.clear()
      selectedTopHeader.visibility = View.GONE
      clearSelection.visibility = View.GONE
    }
  }

  private fun toGroupMovies() = adapter.selectedItemsList.map { m: Movie ->
    GroupMovie(
      name = m.name,
      groupId = group.id,
      rating = m.rating,
      votes = m.votes
    )
  }

  private fun updateCount(
    addMovieButton: MaterialButton,
    clearSelection: ImageView
  ) = { count: Int ->
    if (count > 0) {
      addMovieButton.isEnabled = true
      selectedTopHeader.visibility = View.VISIBLE
      clearSelection.visibility = View.VISIBLE
      selectedTopHeader.text = String.format(selectedTopHeader.text.toString(), count)
    } else {
      addMovieButton.isEnabled = false
      selectedTopHeader.visibility = View.GONE
      clearSelection.visibility = View.GONE
    }
  }
}