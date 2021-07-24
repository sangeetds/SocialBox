package com.socialbox.group.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.socialbox.R
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.group.data.model.Movie
import com.socialbox.login.data.model.User
import com.socialbox.movie.ui.MovieListAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BottomSheetDialog : BottomSheetDialogFragment() {

  lateinit var movieListAdapter: MovieListAdapter
  lateinit var group: Group
  lateinit var url: String
  lateinit var user: User

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    dialog.setOnShowListener {
      Handler().post {
        val bottomSheet = (dialog as? BottomSheetDialog)?.findViewById<View>(R.id.design_bottom_sheet) as? FrameLayout
        bottomSheet?.let {
          BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
      }
    }
    return dialog
  }

  fun toGroupMovies() =
    movieListAdapter.selectedItemsList.map { m: Movie ->
      GroupMovie(
        name = m.name,
        photoURL = m.photoURL ?: "",
        groupId = group.id,
        rating = m.rating,
        votes = m.votes
      )
    }
}