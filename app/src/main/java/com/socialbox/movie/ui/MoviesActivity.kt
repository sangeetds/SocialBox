package com.socialbox.movie.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.socialbox.R
import com.socialbox.group.ui.GroupActivity
import com.socialbox.login.data.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

  private val movieViewModel: MovieViewModel by viewModels()
  private val user: User? by lazy { intent.getParcelableExtra("user") }
  private lateinit var movieViewsAdapter: MovieViewAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.movie_activity_layout)

    val editText: EditText = findViewById(R.id.searchMovieEditText)
    editText.visibility = View.VISIBLE

    val movieViewRecyclerView = findViewById<RecyclerView>(R.id.movieViewRecyclerView)
    movieViewsAdapter = MovieViewAdapter(this, user, movieViewModel)
    movieViewRecyclerView.adapter = movieViewsAdapter
    movieViewRecyclerView.layoutManager = LinearLayoutManager(this)

    findViewById<MaterialButton>(R.id.browseGroupsButton).setOnClickListener {
      startActivity(Intent(this, GroupActivity::class.java))
      finish()
    }
  }
}
