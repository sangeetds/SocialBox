package com.socialbox.movie.ui

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R

class AddMovieDialog : DialogFragment() {

  private var dialogView: View? = null
  private lateinit var adapter: UserMovieListAdapter
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
    val inflate = inflater.inflate(R.layout.add_movie_dialog, container, false)

    topHeader = inflate.findViewById(R.id.top_header)
    selectedTopHeader = inflate.findViewById(R.id.selected_top_header)
    val clearSelection = inflate.findViewById<ImageView>(R.id.clear)
    val addMovieButton: MaterialButton = inflate.findViewById(R.id.addMovieButton)
    val recyclerView: RecyclerView = inflate.findViewById(R.id.movieListRecyclerView)

    val updateCount = { count: Int ->
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
    adapter = UserMovieListAdapter(requireContext(), updateCount)
    val mLayoutManager: LayoutManager = LinearLayoutManager(context)
    recyclerView.layoutManager = mLayoutManager
    recyclerView.itemAnimator = DefaultItemAnimator()
    recyclerView.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
    recyclerView.adapter = adapter
    recyclerView.setHasFixedSize(true)

    addMovieButton.isEnabled = false
    clearSelection.setOnClickListener {
      adapter.selectedItemsList.clear()
      adapter.selectedItems.clear()
      selectedTopHeader.visibility = View.GONE
      clearSelection.visibility = View.GONE
    }

    return inflate
  }

  override fun getView(): View? {
    return dialogView
  }
}