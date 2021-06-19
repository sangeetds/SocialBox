package com.socialbox.movie.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.socialbox.R

class MoviesActivity : AppCompatActivity() {

  private val movieViewModel: MovieViewModel by viewModels()
  private lateinit var personalMovieListAdapter: MovieListAdapter
  private lateinit var latestMovieListAdapter: MovieListAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.movie_activity_layout)

    val personalMovieRecyclerView = findViewById<RecyclerView>(R.id.personalMoviesRecycler)
    val latestMovieRecyclerView = findViewById<RecyclerView>(R.id.latestMoviesRecycler)
    val browseLatestMovies = findViewById<Chip>(R.id.browseLatestMovies)
    val browsePersonalMovies = findViewById<Chip>(R.id.browsePersonalMovies)
    personalMovieListAdapter = MovieListAdapter()
    latestMovieListAdapter = MovieListAdapter()

    personalMovieRecyclerView.adapter = personalMovieListAdapter
    personalMovieRecyclerView.layoutManager = LinearLayoutManager(this)
    personalMovieRecyclerView.setHasFixedSize(true)
    latestMovieRecyclerView.adapter = latestMovieListAdapter
    latestMovieRecyclerView.layoutManager = LinearLayoutManager(this)
    latestMovieRecyclerView.setHasFixedSize(true)

    browseLatestMovies.setOnClickListener {

    }

    browsePersonalMovies.setOnClickListener {

    }

    setUpObservers()
  }

  private fun setUpObservers() {
    movieViewModel.latestMovies.observe(this@MoviesActivity, Observer {
      val movies = it ?: return@Observer
      latestMovieListAdapter.submitList(movies)
    })

    movieViewModel.userMovie.observe(this@MoviesActivity, Observer {
      val movies = it ?: return@Observer
      personalMovieListAdapter.submitList(movies)
    })
  }
}
