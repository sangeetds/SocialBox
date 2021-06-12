package com.socialbox.group.ui

import android.os.Bundle
import android.view.Menu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.socialbox.R
import com.socialbox.R.id
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GroupDetailsActivity : AppCompatActivity() {

  private val groupViewModel: GroupViewModel by viewModels()
  private val movieViewModel: MovieViewModel by viewModels()
  private lateinit var mToolbar: MaterialToolbar
  private lateinit var moviesAdapter: MovieAdapter
  private lateinit var recyclerView: RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.group_details_activity_layout)
    mToolbar = findViewById(id.groupNameBar)
    setSupportActionBar(mToolbar)
    mToolbar.showOverflowMenu()
    supportActionBar?.setDisplayShowTitleEnabled(true)

    moviesAdapter = MovieAdapter()
    recyclerView = findViewById(id.movie_list)
    recyclerView.adapter = moviesAdapter
    recyclerView.layoutManager = GridLayoutManager(this, 2)
    recyclerView.setHasFixedSize(true)

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