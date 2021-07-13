package com.socialbox.group.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.socialbox.R.id
import com.socialbox.R.layout
import com.socialbox.R.menu.top_app_bar
import com.socialbox.common.util.AnimationUtils.Companion.circleReveal
import com.socialbox.login.data.model.User
import com.socialbox.movie.ui.MoviesActivity
import com.socialbox.user.ui.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import me.saket.cascade.CascadePopupWindow
import timber.log.Timber

@AndroidEntryPoint
class GroupActivity : AppCompatActivity() {

  private val search: ConstraintLayout by lazy { findViewById(id.searchTopBar) }
  private val groupViewModel: GroupViewModel by viewModels()
  private lateinit var groupAdapter: GroupAdapter
  private lateinit var emptyText: TextView
  private lateinit var recyclerView: RecyclerView
  private lateinit var toolbar: MaterialToolbar
  private lateinit var user: User

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_group)
    setUpObservables()
    setUpToolbar()
    setUpViews()
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(top_app_bar, menu)
    val userView: MenuItem = menu!!.findItem(id.user)
    val searchView: MenuItem = menu.findItem(id.search)

    userView.setOnMenuItemClickListener {
      val intent = Intent(this, UserActivity::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
      if (search.isVisible) {
        groupViewModel.restoreGroups()
      }
      true
    }

    searchView.setOnMenuItemClickListener {
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
      search.circleReveal(false)
      groupViewModel.restoreGroups()
    }

    eraseQuery.setOnClickListener {
      searchQueryView.text.clear()
    }

    return true
  }

  private fun setUpToolbar() {
    toolbar = findViewById(id.topAppBar)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(true)
    toolbar.setNavigationOnClickListener {
      val popup = CascadePopupWindow(this)
      popup.contentView.addView(NotificationsDialog(this))
      popup.contentView.y = 165F
      popup.contentView.x = 10F
      popup.show(toolbar)
    }
  }

  private fun setUpViews() {
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
        user = user
      ).show(supportFragmentManager.beginTransaction(), "MovieDialog")
    }

    findViewById<MaterialButton>(id.movies_button).setOnClickListener {
      val intent = Intent(this, MoviesActivity::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
      finish()
    }
  }

  private fun setUpObservables() {
    user = intent.extras?.getParcelable("user")!!
    groupViewModel.getGroupsForUser(user.groups.map { it.id!! })
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


