package com.socialbox.movie.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.socialbox.R

class MoviesActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.movie_activity_layout)

    val personalMovieRecyclerView = findViewById<RecyclerView>(R.id.personalMoviesRecycler)
    val latestMovieRecyclerView = findViewById<RecyclerView>(R.id.latestMoviesRecycler)
    val browseLatestMovies = findViewById<Chip>(R.id.browseLatestMovies)
    val browsePersonalMovies = findViewById<Chip>(R.id.browsePersonalMovies)
    val personalMovieListAdapter = MovieListAdapter()
    val latestMovieListAdapter = MovieListAdapter()

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
  }
}
