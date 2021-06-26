package com.socialbox.group.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.socialbox.R.id
import com.socialbox.R.layout
import com.socialbox.R.menu.top_app_bar
import com.socialbox.login.data.model.User
import com.socialbox.movie.ui.MoviesActivity
import com.socialbox.user.ui.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GroupActivity : AppCompatActivity() {

  private val groupViewModel: GroupViewModel by viewModels()
  private lateinit var groupAdapter: GroupAdapter
  private lateinit var emptyText: TextView
  private lateinit var recyclerView: RecyclerView
  private lateinit var notificationBell: MenuItem
  private lateinit var userIcon: MenuItem
  private lateinit var toolbar: MaterialToolbar
  private lateinit var user: User

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.group_activity_layout)

    user = intent.extras?.getParcelable("user") ?: User()

    getGroups()
    setUpToolbar()
    setUpView()
  }

  private fun setUpView() {
    emptyText = findViewById(id.place_holder)
    recyclerView = findViewById(id.group_recycler_view)
    groupAdapter = GroupAdapter(context = this, user = user)
    recyclerView.adapter = groupAdapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.setHasFixedSize(true)
    Timber.i("Recycler view laid out.")

    recyclerView.visibility = View.GONE
    emptyText.visibility = View.VISIBLE

    findViewById<ExtendedFloatingActionButton>(id.new_group_button).setOnClickListener {
      AddGroupDialog(
        viewModel = groupViewModel,
        userId = user.id!!,
        groupIds = user.groupsId!!
      ).show(supportFragmentManager.beginTransaction(), "MovieDialog")
    }

    findViewById<MaterialButton>(id.movies_button).setOnClickListener {
      val intent = Intent(this, MoviesActivity::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
      finish()
    }
  }

  private fun setUpToolbar() {
    toolbar = findViewById(id.topAppBar)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(true)
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(top_app_bar, menu)
    val userView: MenuItem = menu!!.findItem(id.user)

    userView.setOnMenuItemClickListener {
      val intent = Intent(this, UserActivity::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
      true
    }

    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    when (item.itemId) {
      id.search -> {
        TransitionManager.beginDelayedTransition(findViewById(id.groupNameBar))
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
    groupViewModel.getGroupsForUser(user.groupsId!!.toList())
    groupViewModel.groupListState.observe(this@GroupActivity, Observer {
      val groups = it ?: return@Observer

      if (groups.isNotEmpty()) {
        recyclerView.visibility = View.VISIBLE
        emptyText.visibility = View.GONE
        Timber.i("New groups load up: ${groups.joinToString(",") { s -> s.name }}")
        groupAdapter.submitList(groups)
      } else {
        Timber.i("No groups present for the user")
        recyclerView.visibility = View.GONE
        emptyText.visibility = View.VISIBLE
      }
    })
  }
}


