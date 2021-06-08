package com.socialbox.group.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.appbar.MaterialToolbar
import com.socialbox.R
import com.socialbox.group.data.model.Group
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupDetailsActivity : AppCompatActivity() {

  private val groupViewModel: GroupViewModel by viewModels()
  private lateinit var mToolbar: MaterialToolbar
  private lateinit var moviesAdapter: MovieAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_group_details)

    val group = intent.getParcelableExtra<Group>("group")
    groupViewModel.getGroup(group!!.groupId)

    groupViewModel.groupListState.observe(this@GroupDetailsActivity, Observer {

    })
  }

}