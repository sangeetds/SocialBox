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
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.data.model.Group
import com.socialbox.login.data.model.User
import com.socialbox.movie.ui.AddMovieDialog
import com.socialbox.movie.ui.MovieViewModel
import com.socialbox.movie.ui.SearchMovieDialog
import com.socialbox.movie.ui.UserMovieViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GroupDetailsActivity : AppCompatActivity() {

  private val search: ConstraintLayout by lazy { findViewById(id.searchTopBar) }
  private val groupViewModel: GroupViewModel by viewModels()
  private val movieViewModel: MovieViewModel by viewModels()
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
    moviesAdapter = MovieAdapter(url = getString(string.image_base_url))
    recyclerView = findViewById(id.movie_list)
    recyclerView.adapter = moviesAdapter
    recyclerView.layoutManager = GridLayoutManager(this, 2)
    recyclerView.setHasFixedSize(true)

    setUpObservable()
    setUpFabMenu()
    Timber.i("Group Details activity set up.")
  }

  private fun setUpFabMenu() {
    addMovieButton.addActionItem(
      Builder(id.fab_action1, drawable.ic_outline_account_circle_24)
        .setLabel(string.add_movie_from_collection)
        .setLabelBackgroundColor(Color.CYAN)
        .setLabelColor(Color.BLACK)
        .create()
    )
    addMovieButton.addActionItem(
      Builder(id.fab_action2, drawable.ic_baseline_search_24)
        .setLabel(string.search_new_movie)
        .setLabelBackgroundColor(Color.CYAN)
        .setLabelColor(Color.BLACK)
        .create()
    )
    addMovieButton.setOnActionSelectedListener(
      SpeedDialView.OnActionSelectedListener { actionItem ->
        when (actionItem.id) {
          id.add_movie_floating_button -> {
            addMovieButton.close() // To close the Speed Dial with animation
            return@OnActionSelectedListener true // false will close it without animation
          }
          id.fab_action1 -> {
            val user = intent.getStringExtra("user") as User?
            val group = intent.getParcelableExtra<GroupDTO>("groupDTO")
            val addMovieDialog = AddMovieDialog(
              userMovieViewModel,
              groupViewModel,
              user?.id!!,
              groupId = group?.id!!,
              url = this.getString(string.image_base_url)
            )
            addMovieDialog.show(supportFragmentManager.beginTransaction(), "AddMovieDialog")
          }
          id.fab_action2 -> {
            val group = intent.getParcelableExtra<GroupDTO>("groupDTO")
            val searchMovieDialog = SearchMovieDialog(
              movieViewModel,
              group?.id ?: "",
              url = getString(string.image_base_url),
              groupViewModel = groupViewModel
            )
            searchMovieDialog.show(supportFragmentManager.beginTransaction(), "SearchMovieDialog")
          }
        }
        false
      })
  }

  private fun setUpObservable() {
    groupViewModel.getGroup(groupId = (intent.getParcelableExtra<GroupDTO>("groupDTO"))?.id ?: "")
    groupViewModel.groupState.observe(this@GroupDetailsActivity, Observer {
      val groupDetails = it ?: return@Observer

      supportActionBar?.title = groupDetails.name
      if (groupDetails.movieList?.isNotEmpty() == true) {
        moviesAdapter.submitList(groupDetails.movieList)
      }
    })
  }

  override fun onBackPressed() {
    super.onBackPressed()
    finish()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.group_top_app_bar, menu)
    val searchView: MenuItem = menu!!.findItem(id.searchMovieName)
    val toolbar: MaterialToolbar = findViewById(id.groupNameBar)
    val groupDTO = intent.extras?.getParcelable("groupDTO") as GroupDTO?
    val group = intent.extras?.getParcelable("group") as Group?
    toolbar.title = group?.name ?: (groupDTO?.name ?: "SocialBox")

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