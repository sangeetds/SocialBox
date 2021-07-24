package com.socialbox.group.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.viewModels
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
import com.socialbox.login.data.model.User
import com.socialbox.movie.ui.MovieListAdapter
import com.socialbox.movie.ui.UserMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

@AndroidEntryPoint
class AddMovieDialog : BottomSheetDialog() {

  private val userMovieViewModel: UserMovieViewModel by viewModels()
  private val groupViewModel: GroupViewModel by viewModels()
  private lateinit var topHeader: MaterialTextView
  private lateinit var recyclerView: RecyclerView
  private lateinit var selectedTopHeader: MaterialTextView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      group = it.getParcelable(ARG_PARAM1)!!
      url = it.getString(ARG_PARAM2)!!
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)

    return inflater.inflate(layout.bottom_dialog_add_movie, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpViews(view)
    setUpObservables()
  }

  private fun setUpObservables() {
    userMovieViewModel.userMovies.observe(this@AddMovieDialog, Observer {
      val movies = it ?: return@Observer

      movieListAdapter.movies =
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
    movieListAdapter = MovieListAdapter(requireContext(), updateCount, url = url)

    recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    recyclerView.adapter = movieListAdapter
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
      movieListAdapter.selectedItemsList.clear()
      movieListAdapter.selectedItems.clear()
      selectedTopHeader.visibility = View.GONE
      clearSelection.visibility = View.GONE
    }
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

  companion object {
    /**
     * @return A new instance of fragment AddMovieDialog.
     */
    @JvmStatic fun newInstance(param1: Group, param2: String) =
      AddGroupDialog().apply {
        arguments = Bundle().apply {
          putParcelable(ARG_PARAM1, param1)
          putString(ARG_PARAM2, param2)
        }
      }
  }
}