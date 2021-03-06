package com.socialbox.group.ui

import android.content.Context
import android.content.Intent
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
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import com.leinardi.android.speeddial.SpeedDialActionItem.Builder
import com.leinardi.android.speeddial.SpeedDialView
import com.socialbox.R
import com.socialbox.R.drawable
import com.socialbox.R.id
import com.socialbox.R.string
import com.socialbox.chat.ui.ChatActivity
import com.socialbox.chat.ui.ChatSettingsActivity
import com.socialbox.common.util.AnimationUtils.Companion.circleReveal
import com.socialbox.group.data.model.Group
import com.socialbox.login.data.model.User
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GroupDetailsActivity : AppCompatActivity() {

  private val search: ConstraintLayout by lazy { findViewById(id.searchTopBar) }
  private val groupViewModel: GroupViewModel by viewModels()
  private val user: User? by lazy { intent.getParcelableExtra(GROUP) }
  private lateinit var mToolbar: MaterialToolbar
  private lateinit var moviesAdapter: MovieAdapter
  private lateinit var recyclerView: RecyclerView
  private lateinit var addMovieButton: SpeedDialView
  private lateinit var group: Group

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_group_details)
    mToolbar = findViewById(id.groupNameBar)
    setSupportActionBar(mToolbar)
    mToolbar.showOverflowMenu()
    supportActionBar?.setDisplayShowTitleEnabled(true)

    group = intent.getParcelableExtra(GROUP)!!

    addMovieButton = findViewById(id.add_movie_floating_button)
    moviesAdapter = MovieAdapter(url = getString(string.image_base_url))
    recyclerView = findViewById(id.movieList)
    recyclerView.adapter = moviesAdapter
    recyclerView.layoutManager = GridLayoutManager(this, 2)
    recyclerView.setHasFixedSize(true)

    findViewById<MaterialButton>(id.button).setOnClickListener {
      val intent = Intent(this, ChatActivity::class.java)
      intent.putExtra("user", user)
      intent.putExtra("group", group)
      startActivity(intent)
    }

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
            val addMovieDialog =
              AddMovieDialog.newInstance(group, this.getString(string.image_base_url))
            addMovieDialog.show(supportFragmentManager.beginTransaction(), "AddMovieDialog")
          }
          id.fab_action2 -> {
            val searchMovieDialog =
              SearchMovieDialog.newInstance(group, getString(string.image_base_url))
            searchMovieDialog.show(supportFragmentManager.beginTransaction(), "SearchMovieDialog")
          }
        }
        false
      })
  }

  private fun setUpObservable() {
    groupViewModel.getGroup(groupId = group.id)
    val placeholderText = findViewById<MaterialTextView>(id.groupDetailsPlaceholder)
    groupViewModel.groupState.observe(this@GroupDetailsActivity, Observer {
      val groupDetails = it ?: return@Observer

      supportActionBar?.title = groupDetails.name
      group = groupDetails
      if (groupDetails.movieList?.isNotEmpty() == true) {
        recyclerView.visibility = View.VISIBLE
        placeholderText.visibility = View.GONE
        moviesAdapter.submitList(groupDetails.movieList)
      } else {
        recyclerView.visibility = View.GONE
        placeholderText.visibility = View.VISIBLE
      }
    })
  }

  override fun onBackPressed() {
    super.onBackPressed()
    finish()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.group_top_app_bar, menu)
    val searchView: MenuItem = menu!!.findItem(id.search)
    val toolbar: MaterialToolbar = findViewById(id.groupNameBar)
    toolbar.title = group.name ?: getString(string.appName)

    toolbar.setOnClickListener {
      val intent = Intent(this, ChatSettingsActivity::class.java)
      intent.putExtra("group", group)
      intent.putExtra("user", user)
      intent.putExtra("finishActivity", true)
      startActivity(intent)
    }

    searchView.setOnMenuItemClickListener {
      search.visibility = View.VISIBLE
      search.circleReveal(true)
      true
    }

    val searchQueryView = search.findViewById<EditText>(id.searchGroup)
    val cancelSearch = search.findViewById<ImageButton>(id.cancelSearch)
    val eraseQuery = search.findViewById<ImageButton>(id.eraseQuery)

    searchQueryView.doOnTextChanged { text, _, _, _ ->
      // groupViewModel.filterGroupMovies(text ?: "")
      // TODO: Make a GroupDetailsViewModel
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

  companion object {

    private const val GROUP = "group"
    private const val USER = "user"

    fun createIntent(context: Context, newGroup: Group, user: User?) =
      Intent(context, GroupDetailsActivity::class.java).apply {
        putExtra(GROUP, newGroup)
        putExtra(USER, user)
      }
  }
}