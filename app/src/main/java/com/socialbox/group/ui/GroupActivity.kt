package com.socialbox.group.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.socialbox.R.id
import com.socialbox.R.layout
import com.socialbox.R.menu.top_app_bar
import com.socialbox.login.data.model.User
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import android.R

import android.app.SearchManager
import android.content.Context
import kotlin.Int.Companion

@AndroidEntryPoint
class GroupActivity : AppCompatActivity() {

  private val groupViewModel: GroupViewModel by viewModels()
  private lateinit var groupAdapter: GroupAdapter
  private lateinit var newMovieButton: ExtendedFloatingActionButton
  private lateinit var emptyText: TextView
  private lateinit var recyclerView: RecyclerView
  private lateinit var notificationBell: MenuItem
  private lateinit var userIcon: MenuItem
  private lateinit var mToolbar: MaterialToolbar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.group_activity_layout)
    mToolbar = findViewById(id.topAppBar)
    setSupportActionBar(mToolbar)
    supportActionBar?.setDisplayShowTitleEnabled(true)

    val user: User? = intent.extras?.getParcelable("user")

    groupViewModel.getGroupsForUser(user?.id!!)
    getGroups()

    groupAdapter = GroupAdapter(context = this, user)
    emptyText = findViewById(id.place_holder)
    recyclerView = findViewById(id.group_recycler_view)
    newMovieButton = findViewById(id.new_group_button)
    recyclerView.adapter = groupAdapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.setHasFixedSize(true)
    Timber.i("Recycler view laid out.")

    recyclerView.visibility = View.GONE
    emptyText.visibility = View.VISIBLE

    newMovieButton.setOnClickListener {
      AddMovieDialog(viewModel = groupViewModel, userId = user.id).show(
        supportFragmentManager.beginTransaction(), "MovieDialog"
      )
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(top_app_bar, menu)
    val searchView: SearchView = menu!!.findItem(id.search).actionView as SearchView
    val closeButton = searchView.findViewById<ImageView>(id.search_close_btn)
    notificationBell = menu.findItem(id.notification)
    userIcon = menu.findItem(id.user)

    val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
    searchView.setSearchableInfo(
      searchManager
        .getSearchableInfo(componentName)
    )

    searchView.setOnClickListener {
      TransitionManager.beginDelayedTransition(findViewById(id.topAppBar))
      notificationBell.isVisible = false
      Timber.i("${notificationBell.isVisible}")
      userIcon.isVisible = false
    }


    searchView.queryHint = "Search Groups.."
    searchView.setOnQueryTextListener(object : OnQueryTextListener {
      override fun onQueryTextSubmit(query: String): Boolean {
        groupAdapter.filter.filter(query)
        return false
      }

      override fun onQueryTextChange(query: String): Boolean {
        groupAdapter.filter.filter(query)
        return false
      }
    })

    closeButton.setOnClickListener {
      notificationBell.isVisible = true
      notificationBell.isVisible = true
      searchView.onActionViewCollapsed()
    }

    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      id.search -> {
        TransitionManager.beginDelayedTransition(findViewById(id.topAppBar))
        item.expandActionView()
        notificationBell.isVisible = false
        Timber.i("${notificationBell.isVisible}")
        userIcon.isVisible = false
        return true
      }
    }
    return super.onOptionsItemSelected(item)
  }

  private fun getGroups() {
    groupViewModel.groupListState.observe(this@GroupActivity, Observer {
      val groups = it ?: return@Observer

      if (groups.isNotEmpty()) {
        recyclerView.visibility = View.VISIBLE
        emptyText.visibility = View.GONE
        Timber.i("New groups load up: ${groups.joinToString(",") { s -> s.groupName }}")
        groupAdapter.groupList.addAll(groups)
        groupAdapter.notifyDataSetChanged()
      } else {
        recyclerView.visibility = View.GONE
        emptyText.visibility = View.VISIBLE
      }
    })
  }
}


