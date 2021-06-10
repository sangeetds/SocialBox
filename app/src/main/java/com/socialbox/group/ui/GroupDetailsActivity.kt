package com.socialbox.group.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.socialbox.R
import com.socialbox.R.id
import com.socialbox.group.data.model.Group
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailsActivity : AppCompatActivity() {

  private val groupViewModel: GroupViewModel by viewModels()
  private lateinit var mToolbar: MaterialToolbar
  private lateinit var moviesAdapter: MovieAdapter
  private lateinit var recyclerView: RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.group_details_activity_layout)
    mToolbar = findViewById(id.groupNameBar)
    setSupportActionBar(mToolbar)
    supportActionBar?.setDisplayShowTitleEnabled(true)

    moviesAdapter = MovieAdapter()
    recyclerView = findViewById(id.movie_list)
    recyclerView.adapter = moviesAdapter
    recyclerView.setHasFixedSize(true)
    recyclerView.layoutManager = GridLayoutManager(this, 2)

    val group = intent.getParcelableExtra<Group>("group")
    groupViewModel.getGroup(group!!.groupId)
    mToolbar.title = group.groupName
    supportActionBar?.title = group.groupName

    groupViewModel.groupState.observe(this@GroupDetailsActivity, Observer {
      val groupDetails = it ?: return@Observer

      if (groupDetails.groupMovieList.isNotEmpty()) {
        moviesAdapter.cardList = groupDetails.groupMovieList
        moviesAdapter.notifyDataSetChanged()
      }
    })
  }
}