package com.socialbox.group.ui

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.socialbox.R
import com.socialbox.R.id
import com.socialbox.login.data.model.User
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class GroupActivity : AppCompatActivity() {

  private val groupViewModel: GroupViewModel by viewModels()
  private lateinit var groupAdapter: GroupAdapter
  private lateinit var emptyText: TextView
  private lateinit var recyclerView: RecyclerView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.group_activity_layout)

    val user: User? = intent.extras?.getParcelable("user")

    groupAdapter = GroupAdapter(context = this, user)
    recyclerView = findViewById(id.group_recycler_view)
    recyclerView.adapter = groupAdapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.setHasFixedSize(true)
    Timber.i("Recycler view laid out.")

    getGroups()
  }

  private fun getGroups() {
    groupViewModel.groupState.observe(this@GroupActivity, Observer {
      val groups = it ?: return@Observer
      Timber.i("New songs loaded up.")

      if (groups.isNotEmpty()) {
        Timber.i("New songs load up: ${groups.joinToString(",") { s -> s.groupName }}")
        groupAdapter.groupList.addAll(groups)
        groupAdapter.notifyDataSetChanged()
      }
      else {
        recyclerView.visibility = View.GONE
        emptyText.visibility = View.VISIBLE
      }
    })
  }
}


