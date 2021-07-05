package com.socialbox.movie.ui

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.group.data.model.Movie
import com.socialbox.group.ui.GroupViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchMovieDialog(
  private val movieViewModel: MovieViewModel,
  val groupId: String,
  private val groupViewModel: GroupViewModel,
  private val url: String
) :
  DialogFragment() {

  private var dialogView: View? = null
  // private lateinit var adapter: MovieListAdapter
  // private lateinit var recyclerView: RecyclerView
  // private lateinit var topHeader: EditText
  // private lateinit var updateCount: ((Int) -> Unit)
  // private lateinit var selectedTopHeader: MaterialTextView

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
    val recyclerView = inflate.findViewById<RecyclerView>(R.id.searchMovieRecycleView)
    val searchBar = inflate.findViewById<EditText>(R.id.searchMovieName)
    val addMovieButton = inflate.findViewById<MaterialButton>(R.id.addMovie)
    val selectedTopHeader = inflate.findViewById<MaterialTextView>(R.id.selectedTopHeader)
    val clearSelection = inflate.findViewById<ImageView>(R.id.clearSelection)
    val updateCount = updateCount(addMovieButton, clearSelection, selectedTopHeader, searchBar)

    val movieListAdapter = MovieListAdapter(requireContext(), updateCount, url)
    recyclerView.adapter = movieListAdapter
    recyclerView.layoutManager = LinearLayoutManager(requireContext())

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
      groupViewModel.getGroup(groupId)
    }

    return inflate
  }

  // private fun setUpViews(inflate: View) {
  //   val clearSelection = inflate.findViewById<ImageView>(R.id.clear)
  //   val addMovieButton = inflate.findViewById<MaterialButton>(R.id.addMovie)
  //   // updateCount = updateCount(addMovieButton, clearSelection)
  //   topHeader = inflate.findViewById(R.id.searchMovieName)
  //   selectedTopHeader = inflate.findViewById(R.id.selected_top)
  //
  //   addMovieButton.setOnClickListener {
  //     val groupMovies = toGroupMovies()
  //     if (toGroupMovies().isNotEmpty()) {
  //       groupViewModel.addMovies(groupMovies)
  //       dismiss()
  //     }
  //   }
  //
  //   clearSelection.setOnClickListener {
  //     adapter.selectedItemsList.clear()
  //     adapter.selectedItems.clear()
  //     selectedTopHeader.visibility = View.GONE
  //     clearSelection.visibility = View.GONE
  //   }
  // }

  override fun getView(): View? {
    return dialogView
  }

  private fun toGroupMovies(movieListAdapter: MovieListAdapter) =
    movieListAdapter.selectedItemsList.map { m: Movie ->
      GroupMovie(
        name = m.name,
        photoUrl = m.photoURL ?: "",
        groupId = groupId,
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
