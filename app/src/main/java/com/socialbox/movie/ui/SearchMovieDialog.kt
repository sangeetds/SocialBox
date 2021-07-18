package com.socialbox.movie.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.R.layout
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.group.data.model.Movie
import com.socialbox.group.ui.GroupViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchMovieDialog(
  private val movieViewModel: MovieViewModel,
  private val group: Group,
  private val groupViewModel: GroupViewModel,
  private val url: String
) :
  BottomSheetDialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)

    return inflater.inflate(layout.dialog_search_movie, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    val recyclerView = view.findViewById<RecyclerView>(R.id.searchMovieRecycleView)
    val searchBar = view.findViewById<EditText>(R.id.searchMovieName)
    val addMovieButton = view.findViewById<MaterialButton>(R.id.addMovie)
    val selectedTopHeader = view.findViewById<MaterialTextView>(R.id.selectedTopHeader)
    val clearSelection = view.findViewById<ImageView>(R.id.clearSelection)
    val updateCount = updateCount(addMovieButton, clearSelection, selectedTopHeader, searchBar)

    val movieListAdapter = MovieListAdapter(requireContext(), updateCount, url)
    recyclerView.adapter = movieListAdapter
    recyclerView.layoutManager = LinearLayoutManager(requireContext())
    recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    recyclerView.itemAnimator = DefaultItemAnimator()

    searchBar.addTextChangedListener(object : TextWatcher {
      private var searchFor = searchBar.text.toString()
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val searchText = s.toString().trim()
        if (searchFor != searchText) {
          searchFor = searchText
          CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            if (searchText == searchFor) {
              movieViewModel.getUserMovies(searchText)
            }
          }
        }
      }

      override fun afterTextChanged(s: Editable?) {}
    })

    clearSelection.setOnClickListener {
      movieListAdapter.clearSelection()
      selectedTopHeader.visibility = View.GONE
      clearSelection.visibility = View.GONE
      searchBar.visibility = View.VISIBLE
    }

    movieViewModel.movies.observe(this@SearchMovieDialog, Observer {
      val m = it ?: return@Observer
      movieListAdapter.movies = m
      recyclerView.adapter?.notifyDataSetChanged()
      movieListAdapter.notifyDataSetChanged()
    })

    addMovieButton.isEnabled = false
    addMovieButton.setOnClickListener {
      val groupMovies = toGroupMovies(movieListAdapter)
      if (groupMovies.isNotEmpty()) {
        groupViewModel.addMovies(groupMovies)
        dialog?.dismiss()
      }
      groupViewModel.getGroup(group.id)
    }
  }

  private fun toGroupMovies(movieListAdapter: MovieListAdapter) =
    movieListAdapter.selectedItemsList.map { m: Movie ->
      GroupMovie(
        name = m.name,
        photoURL = m.photoURL ?: "",
        groupId = group.id,
        rating = m.rating,
        votes = m.votes
      )
    }

  private fun updateCount(
    addMovieButton: MaterialButton,
    clearSelection: ImageView,
    selectedTopHeader: MaterialTextView,
    searchBar: EditText
  ) = { count: Int ->
    if (count > 0) {
      addMovieButton.isEnabled = true
      selectedTopHeader.visibility = View.VISIBLE
      clearSelection.visibility = View.VISIBLE
      selectedTopHeader.text = getString(R.string.s_selected).format(count)
      searchBar.visibility = View.GONE
    } else {
      addMovieButton.isEnabled = false
      selectedTopHeader.visibility = View.GONE
      searchBar.visibility = View.VISIBLE
      clearSelection.visibility = View.GONE
    }
  }
}
