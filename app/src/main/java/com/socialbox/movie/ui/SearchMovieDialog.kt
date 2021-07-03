package com.socialbox.movie.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.group.data.model.Movie
import com.socialbox.group.ui.GroupViewModel

class SearchMovieDialog(
  private val movieViewModel: MovieViewModel,
  val groupId: String,
  private val groupViewModel: GroupViewModel
) :
  DialogFragment() {

  private var dialogView: View? = null
  private lateinit var adapter: MovieListAdapter
  private lateinit var topHeader: MaterialTextView
  private lateinit var selectedTopHeader: MaterialTextView

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return MaterialAlertDialogBuilder(requireContext(), theme).apply {
      dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
      dialogView?.let { onViewCreated(it, savedInstanceState) }
      setView(dialogView)
    }.create()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?,
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    val inflate = inflater.inflate(R.layout.search_movie_dialog, container, false)

    setUpViews(inflate)
    setUpObservables()

    return inflate
  }

  private fun setUpObservables() {
    movieViewModel.movies.observe(this@SearchMovieDialog, Observer {
      val movies = it ?: return@Observer

      adapter.submitList(movies)
    })
  }

  private fun setUpViews(inflate: View) {
    val clearSelection = inflate.findViewById<ImageView>(R.id.clear)
    val addMovieButton = inflate.findViewById<MaterialButton>(R.id.addMovieButton)
    val recyclerView = inflate.findViewById<RecyclerView>(R.id.movieListRecyclerView)
    val mLayoutManager: LayoutManager = LinearLayoutManager(context)
    val updateCount = updateCount(addMovieButton, clearSelection)
    topHeader = inflate.findViewById(R.id.searchMoviesText)
    selectedTopHeader = inflate.findViewById(R.id.selected_top_header)
    adapter = MovieListAdapter(requireContext(), updateCount)

    recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    recyclerView.layoutManager = mLayoutManager
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.adapter = adapter
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

    topHeader.doOnTextChanged { text, _, _, _ ->
      text?.let { movieViewModel.getUserMovies(it.toString()) }
    }
  }

  override fun getView(): View? {
    return dialogView
  }

  private fun toGroupMovies() = adapter.selectedItemsList.map { m: Movie ->
    GroupMovie(
      name = m.name,
      groupId = groupId,
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
