package com.socialbox.movie.ui

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.R.id
import com.socialbox.R.style
import com.socialbox.common.enums.Genre
import com.socialbox.common.enums.Result.Success
import com.socialbox.group.data.model.Movie
import com.socialbox.group.ui.GroupActivity
import com.socialbox.login.data.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

  private val movieViewModel: MovieViewModel by viewModels()
  private val user: User? by lazy { intent.getParcelableExtra("user") }
  private val movieViewRecyclerView: RecyclerView by lazy { findViewById(id.movieViewRecyclerView) }
  private val searchMoviesRecyclerView: RecyclerView by lazy { findViewById(id.searchMoviesRecyclerView) }
  private val searchTextHolder: MaterialTextView by lazy { findViewById(id.searchPlaceHolder) }
  private lateinit var movieViewsAdapter: MovieViewAdapter
  private var searchScreenPresent: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_movie)

    movieViewsAdapter = MovieViewAdapter(this, user, movieViewModel, setSearchMode)
    movieViewRecyclerView.adapter = movieViewsAdapter
    movieViewRecyclerView.layoutManager = LinearLayoutManager(this)

    findViewById<MaterialButton>(id.browseGroupsButton).setOnClickListener {
      val intent = Intent(this, GroupActivity::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
      finish()
    }

    movieViewModel.documentaryMovies.observe(this, Observer {
      val movies = it ?: return@Observer
      val views = movieViewsAdapter.views
      val data: List<Movie> = (movies as Success).data
      movieViewsAdapter.views = views + listOf(data)
      movieViewsAdapter.notifyItemChanged(views.size + 1)
    })
  }

  override fun onBackPressed() {
    if (searchScreenPresent) {
      collapseSearchView()
      presentRecyclerView()
    } else {
      super.onBackPressed()
      finish()
    }
  }

  private val setSearchMode = { genre: Genre ->
    fadeRecyclerView()
    expandSearchView(genre)
    val searchMovieListAdapter =
      MovieDisplayAdapter(context = this)
    searchMoviesRecyclerView.adapter = searchMovieListAdapter
    searchMoviesRecyclerView.layoutManager = GridLayoutManager(this, 3)
    searchMoviesRecyclerView.visibility = View.VISIBLE
    movieViewModel.movies.observe(this, Observer {
      val movieList = it ?: return@Observer
      if (movieList.isNotEmpty()) {
        searchTextHolder.visibility = View.GONE
      }
      searchMovieListAdapter.submitList(movieList)
    })
  }

  private fun expandSearchView(genre: Genre) {
    searchScreenPresent = true
    val toolBarView: MaterialToolbar = findViewById(id.material_tool_bar)
    val slideAnimator = ValueAnimator
      .ofInt(toolBarView.height, toolBarView.height + 100)
      .setDuration(1000)

    slideAnimator.addUpdateListener { animation1 ->
      val value = animation1.animatedValue as Int
      toolBarView.layoutParams.height = value
      toolBarView.requestLayout()
    }

    val animationSet = AnimatorSet()
    animationSet.interpolator = AccelerateDecelerateInterpolator()
    animationSet.play(slideAnimator)
    animationSet.start()
    val editText: EditText = findViewById(id.searchMovieEditText)
    editText.visibility = View.VISIBLE

    editText.addTextChangedListener(object : TextWatcher {
      private var searchFor = editText.text.toString()
      override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

      override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val searchText = s.toString().trim()
        if (searchFor != searchText) {
          searchFor = searchText
          CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            if (searchText == searchFor) {
              searchTextHolder.visibility = View.VISIBLE
              movieViewModel.searchAllMovies(searchText, genre)
            }
          }
        }
      }

      override fun afterTextChanged(s: Editable?) {}
    })

    toolBarView.setTitleTextAppearance(this@MoviesActivity, style.Toolbar_TitleText)
  }

  private fun collapseSearchView() {
    searchScreenPresent = false
    searchMoviesRecyclerView.visibility = View.GONE
    val toolBarView: MaterialToolbar = findViewById(id.material_tool_bar)
    val slideAnimator = ValueAnimator
      .ofInt(toolBarView.height, toolBarView.height - 100)
      .setDuration(1000)

    slideAnimator.addUpdateListener { animation1 ->
      val value = animation1.animatedValue as Int
      toolBarView.layoutParams.height = value
      toolBarView.requestLayout()
    }

    val animationSet = AnimatorSet()
    animationSet.interpolator = AccelerateDecelerateInterpolator()
    animationSet.play(slideAnimator)
    animationSet.start()
    val editText: EditText = findViewById(id.searchMovieEditText)
    editText.visibility = View.GONE
    toolBarView.setTitleTextAppearance(this@MoviesActivity, style.Toolbar_ReverseTitleText)
  }

  private fun fadeRecyclerView() {
    val transition: Transition = Fade()
    transition.duration = 300
    transition.addTarget(id.movieViewRecyclerView)
    TransitionManager.beginDelayedTransition(
      findViewById(id.movieConstraintLayout),
      transition
    )
    movieViewRecyclerView.visibility = View.GONE
  }

  private fun presentRecyclerView() {
    val transition: Transition = Fade()
    transition.duration = 300
    transition.addTarget(id.movieViewRecyclerView)
    TransitionManager.beginDelayedTransition(
      findViewById(id.movieConstraintLayout),
      transition
    )
    movieViewRecyclerView.visibility = View.VISIBLE
  }
}
