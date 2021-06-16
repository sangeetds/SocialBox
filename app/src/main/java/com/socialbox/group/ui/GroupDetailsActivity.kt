package com.socialbox.group.ui

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
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
import com.socialbox.movie.ui.AddMovieDialog
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GroupDetailsActivity : AppCompatActivity() {

  private val groupViewModel: GroupViewModel by viewModels()
  private val movieViewModel: MovieViewModel by viewModels()
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
        .setLabelColor(Color.WHITE)
        .create())
    addMovieButton.addActionItem(
      Builder(id.fab_action2, drawable.ic_baseline_search_24)
        .setLabel(string.search_new_movie)
        .setLabelColor(Color.WHITE)
        .create())
    addMovieButton.setOnActionSelectedListener(
      SpeedDialView.OnActionSelectedListener { actionItem ->
        when (actionItem.id) {
          id.add_movie_floating_button -> {
            addMovieButton.close() // To close the Speed Dial with animation
            return@OnActionSelectedListener true // false will close it without animation
          }
          id.fab_action1 -> {
            AddMovieDialog().show(supportFragmentManager.beginTransaction(), "AddMovieDialog")
          }
          id.fab_action2 -> { }
        }
        false
      })
  }

  private fun setUpObservable() {
    val groupId = intent.getStringExtra("groupId")
    groupViewModel.getGroup(groupId!!)
    movieViewModel.getAllMovies()

    movieViewModel.movieState.observe(this@GroupDetailsActivity, Observer {
      val movie = it ?: return@Observer
      Timber.i("Adding movies to the adapter: ${movie.joinToString(",") { m -> m.movieName }}")
      moviesAdapter.movies = movie
      moviesAdapter.notifyDataSetChanged()
    })

    groupViewModel.groupState.observe(this@GroupDetailsActivity, Observer {
      val groupDetails = it ?: return@Observer

      supportActionBar?.title = groupDetails.groupName
      // if (groupDetails.groupMovieList?.isNotEmpty() == true) {
      //   moviesAdapter.submitList(groupDetails.groupMovieList)
      // }
    })
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.top_app_bar, menu)
    return super.onCreateOptionsMenu(menu)
  }
}