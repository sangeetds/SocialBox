package com.socialbox.group.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.leinardi.android.speeddial.SpeedDialActionItem.Builder
import com.leinardi.android.speeddial.SpeedDialView
import com.socialbox.R
import com.socialbox.R.drawable
import com.socialbox.R.id
import com.socialbox.R.string
import com.socialbox.common.util.AnimationUtils.Companion.circleReveal
import com.socialbox.movie.ui.AddMovieDialog
import com.socialbox.movie.ui.UserMovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailsActivity : AppCompatActivity() {

  private val search: ConstraintLayout by lazy { findViewById(id.searchTopBar) }
  private val groupViewModel: GroupViewModel by viewModels()
  private val groupDetailsViewModel: GroupDetailsViewModel by viewModels()
  private val userMovieViewModel: UserMovieViewModel by viewModels()
  private lateinit var mToolbar: MaterialToolbar
  private lateinit var moviesAdapter: MovieAdapter
  private lateinit var recyclerView: RecyclerView
  private lateinit var addMovieButton: SpeedDialView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.group_details_activity_layout)
    mToolbar = findViewById(id.groupNameBar)
    setSupportActionBar(mToolbar)
    mToolbar.showOverflowMenu()
    supportActionBar?.setDisplayShowTitleEnabled(true)

    addMovieButton = findViewById(id.add_movie_floating_button)
    moviesAdapter = MovieAdapter()
    recyclerView = findViewById(id.movie_list)
    recyclerView.adapter = moviesAdapter
    recyclerView.layoutManager = GridLayoutManager(this, 2)
    recyclerView.setHasFixedSize(true)

    setUpObservable()
    setUpFabMenu()
  }

  private fun setUpFabMenu() {
    addMovieButton.addActionItem(
      Builder(id.fab_action1, drawable.ic_outline_account_circle_24)
        .setLabel(string.add_movie_from_collection)
        .setLabelBackgroundColor(Color.CYAN)
        .setLabelColor(Color.BLACK)
        .create())
    addMovieButton.addActionItem(
      Builder(id.fab_action2, drawable.ic_baseline_search_24)
        .setLabel(string.search_new_movie)
        .setLabelBackgroundColor(Color.CYAN)
        .setLabelColor(Color.BLACK)
        .create())
    addMovieButton.setOnActionSelectedListener(
      SpeedDialView.OnActionSelectedListener { actionItem ->
        when (actionItem.id) {
          id.add_movie_floating_button -> {
            addMovieButton.close() // To close the Speed Dial with animation
            return@OnActionSelectedListener true // false will close it without animation
          }
          id.fab_action1 -> {
            val userId = intent.getStringExtra("userId") ?: ""
            val groupId = intent.getStringExtra("groupId") ?: ""
            val addMovieDialog = AddMovieDialog(userMovieViewModel, userId, groupId)
            addMovieDialog.show(supportFragmentManager.beginTransaction(), "AddMovieDialog")
            addMovieDialog.dialog?.setOnDismissListener {
              groupViewModel.getGroup(groupId)
            }
          }
          id.fab_action2 -> { }
        }
        false
      })
  }

  private fun setUpObservable() {
    val groupId = intent.getStringExtra("groupId")

    groupViewModel.groupState.observe(this@GroupDetailsActivity, Observer {
      val groupDetails = it ?: return@Observer

      supportActionBar?.title = groupDetails.name
      if (groupDetails.movieList?.isNotEmpty() == true) {
        moviesAdapter.submitList(groupDetails.movieList)
      }
    })
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.group_top_app_bar, menu)
    val searchView: MenuItem = menu!!.findItem(id.search)

    searchView.setOnMenuItemClickListener {
      search.visibility = View.VISIBLE
      search.circleReveal(true)
      true
    }

    val searchQueryView = search.findViewById<EditText>(id.searchGroup)
    val cancelSearch = search.findViewById<ImageButton>(id.cancelSearch)
    val eraseQuery = search.findViewById<ImageButton>(id.eraseQuery)

    searchQueryView.doOnTextChanged { text, _, _, _ ->
      groupViewModel.filterGroups(text ?: "")
    }

    cancelSearch.setOnClickListener {
      search.visibility = View.GONE
      search.circleReveal(false)
      groupViewModel.restoreGroups()
    }

    eraseQuery.setOnClickListener {
      searchQueryView.text.clear()
    }

    return super.onCreateOptionsMenu(menu)
  }
}