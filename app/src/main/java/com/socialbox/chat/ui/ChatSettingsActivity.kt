package com.socialbox.chat.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R.id
import com.socialbox.R.layout
import com.socialbox.R.string
import com.socialbox.group.data.model.Group
import com.socialbox.group.ui.GroupDetailsActivity
import com.socialbox.login.data.model.User
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChatSettingsActivity : AppCompatActivity() {

  private val group: Group? by lazy { intent.getParcelableExtra("group") }
  private val user: User? by lazy { intent.getParcelableExtra("user") }
  private val finishActivity: Boolean? by lazy { intent.getBooleanExtra("finishActivity", false) }
  private val chatViewModel: ChatViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_chat_settings)
    // group?.users ?: setOf()  Todo: Fetch Users for the group

    setMembersLayout()
    setUpMovies()
    setUpObservers()
  }

  private fun setUpMovies() {
    val movieTwo: MaterialCardView = findViewById(id.movieTwo)
    val movieThree: MaterialCardView = findViewById(id.movieThree)
    val groupMovies = group?.movieList ?: listOf()

    val viewAllMovies: MaterialTextView = findViewById(id.viewAllMovies)
    if (groupMovies.size < 3) movieThree.visibility = View.GONE
    if (groupMovies.size < 2) movieTwo.visibility = View.GONE
    if (groupMovies.isEmpty()) {
      findViewById<RelativeLayout>(id.moviesLayout).visibility = View.GONE
    }

    viewAllMovies.setOnClickListener {
      if (finishActivity!!) {
        finish()
      }
      else {
        val intent = Intent(this, GroupDetailsActivity::class.java)
        intent.putExtra("group", group)
        intent.putExtra("user", user)
        startActivity(intent)
        finish()
      }
    }

    if (groupMovies.isNotEmpty()) {
      Picasso.get().load("${getString(string.image_base_url)}${groupMovies[0].photoURL}")
        .into(findViewById<ImageView>(id.movieOneImage))
    }
    if (groupMovies.size > 1) {
      Picasso.get().load("${getString(string.image_base_url)}${groupMovies[1].photoURL}")
        .into(findViewById<ImageView>(id.movieTwoImage))
    }
    if (groupMovies.size > 2) {
      Picasso.get().load("${getString(string.image_base_url)}${groupMovies[2].photoURL}")
        .into(findViewById<ImageView>(id.movieThreeImage))
    }
  }

  private fun setUpObservers() {
    findViewById<MaterialTextView>(id.addMember).setOnClickListener {
      chatViewModel.getInviteLink(groupId = group!!.id!!, userId = user!!.id!!)
    }

    chatViewModel.inviteState.observe(this, Observer {
      val inviteLink = it ?: return@Observer

      val sendIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "${inviteLink.content} ${inviteLink.link!!.url}/${group!!.id}")
        type = "text/plain"
      }

      val shareIntent = Intent.createChooser(sendIntent, null)
      startActivity(shareIntent)
    })
  }

  private fun setMembersLayout() {
    val memberOne: MaterialCardView = findViewById(id.memberOne)
    val memberTwo: MaterialCardView = findViewById(id.memberTwo)
    val memberThree: MaterialCardView = findViewById(id.memberThree)
    val memberFour: MaterialCardView = findViewById(id.memberFour)
    val memberFive: MaterialCardView = findViewById(id.memberFive)
    val memberSix: MaterialCardView = findViewById(id.memberSix)
    val layout = findViewById<ConstraintLayout>(id.membersConstrainView)
    val userName: MaterialTextView = findViewById(id.userName)
    val viewAllMembers: MaterialTextView = findViewById(id.viewAllMembers)
    val groupMembers = setOf<User>() // Todo: Fetch Users

    if (groupMembers.size < 6) memberSix.visibility = View.GONE
    if (groupMembers.size < 5) {
      memberFive.visibility = View.GONE
      layout.setPadding(
        layout.paddingStart,
        layout.paddingTop,
        layout.paddingEnd * 2,
        layout.paddingBottom
      )
    }
    if (groupMembers.size < 4) memberFour.visibility = View.GONE
    if (groupMembers.size < 3) {
      memberThree.visibility = View.GONE
      layout.setPadding(
        layout.paddingStart,
        layout.paddingTop,
        layout.paddingEnd * 2,
        layout.paddingBottom
      )
    }
    if (groupMembers.size < 2) memberTwo.visibility = View.GONE
    if (groupMembers.isEmpty()) memberOne.visibility = View.GONE

    userName.text = user?.name

    viewAllMembers.setOnClickListener {
      val relativeLayout: RelativeLayout = findViewById(id.relativeLayout)
      val recyclerView: RecyclerView = findViewById(id.membersRecyclerView)
      val adapter = MembersAdapter(listOf(user!!))
      val backButton: MaterialTextView = findViewById(id.backButton)
      val recyclerContainer = findViewById<ConstraintLayout>(id.recyclerContainer)

      backButton.setOnClickListener {
        relativeLayout.visibility = View.VISIBLE
        recyclerContainer.visibility = View.GONE
      }

      relativeLayout.visibility = View.GONE
      recyclerContainer.visibility = View.VISIBLE
      recyclerView.adapter = adapter
      recyclerView.layoutManager = LinearLayoutManager(this)
      recyclerView.setHasFixedSize(true)
    }
  }
}