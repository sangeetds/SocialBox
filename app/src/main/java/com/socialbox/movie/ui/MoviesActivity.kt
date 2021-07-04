package com.socialbox.movie.ui

import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.socialbox.R
import com.socialbox.R.id
import com.socialbox.R.style
import com.socialbox.group.ui.GroupActivity
import com.socialbox.login.data.model.User
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MoviesActivity : AppCompatActivity() {

  private val movieViewModel: MovieViewModel by viewModels()
  private val user: User? by lazy { intent.getParcelableExtra("user") }
  private val movieViewRecyclerView: RecyclerView by lazy { findViewById(R.id.movieViewRecyclerView) }
  private lateinit var movieViewsAdapter: MovieViewAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.movie_activity_layout)

    movieViewsAdapter = MovieViewAdapter(this, user, movieViewModel, setSearchMode)
    movieViewRecyclerView.adapter = movieViewsAdapter
    movieViewRecyclerView.layoutManager = LinearLayoutManager(this)

    findViewById<MaterialButton>(id.browseGroupsButton).setOnClickListener {
      val intent = Intent(this, GroupActivity::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
      finish()
    }
  }

  private val setSearchMode = {
    fadeRecyclerView()
    expandSearchView()
    val searchMovieListAdapter = MovieListAdapter(context = this, updateCount = null)
    val searchMoviesRecyclerView = findViewById<RecyclerView>(R.id.searchMoviesRecyclerView)
    searchMoviesRecyclerView.adapter = searchMovieListAdapter
    searchMoviesRecyclerView.layoutManager = LinearLayoutManager(this)
  }

  private fun expandSearchView() {
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
    toolBarView.setTitleTextAppearance(this@MoviesActivity, style.Toolbar_TitleText)
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
}
