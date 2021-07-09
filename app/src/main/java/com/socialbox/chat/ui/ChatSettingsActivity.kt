package com.socialbox.chat.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.group.data.model.Group
import com.socialbox.group.ui.GroupDetailsActivity
import com.socialbox.login.data.model.User

class ChatSettingsActivity : AppCompatActivity() {

  private val group by lazy { intent.getParcelableExtra<Group>("group") }
  private val user by lazy { intent.getParcelableExtra<User>("user") }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat_settings)

    val memberOne: MaterialCardView = findViewById(R.id.memberOne)
    val memberTwo: MaterialCardView = findViewById(R.id.memberTwo)
    val memberThree: MaterialCardView = findViewById(R.id.memberThree)
    val memberFour: MaterialCardView = findViewById(R.id.memberFour)
    val memberFive: MaterialCardView = findViewById(R.id.memberFive)
    val memberSix: MaterialCardView = findViewById(R.id.memberSix)
    val memberSelf: MaterialCardView = findViewById(R.id.memberSelf)

    val movieOne: MaterialCardView = findViewById(R.id.movieOne)
    val movieTwo: MaterialCardView = findViewById(R.id.movieTwo)
    val movieThree: MaterialCardView = findViewById(R.id.movieThree)

    val userName: MaterialTextView = findViewById(R.id.userName)
    val userNameBold: MaterialTextView = findViewById(R.id.userNameBold)
    val viewAllMembers: MaterialTextView = findViewById(R.id.viewAllMembers)
    val viewAllMovies: MaterialTextView = findViewById(R.id.viewAllMovies)
    val addMember: MaterialTextView = findViewById(R.id.addMember)
    val leaveGroup: MaterialTextView = findViewById(R.id.leaveGroup)

    val groupMembers = group?.usersId ?: setOf()
    val groupMovies = group?.movieList ?: listOf()

    if (groupMembers.size < 6) memberSix.visibility = View.GONE
    if (groupMembers.size < 5) memberFive.visibility = View.GONE
    if (groupMembers.size < 4) memberFour.visibility = View.GONE
    if (groupMembers.size < 3) memberThree.visibility = View.GONE
    if (groupMembers.size < 2) memberTwo.visibility = View.GONE
    if (groupMembers.isEmpty()) memberOne.visibility = View.GONE

    if (groupMovies.size < 3) movieThree.visibility = View.GONE
    if (groupMovies.size < 2) movieTwo.visibility = View.GONE
    if (groupMovies.isEmpty()) movieOne.visibility = View.GONE

    userName.text = user?.name
    userNameBold.text = user?.name

    viewAllMembers.setOnClickListener {
      val relativeLayout: RelativeLayout = findViewById(R.id.relativeLayout)
      val recyclerView: RecyclerView = findViewById(R.id.membersRecyclerView)
      val adapter = MembersAdapter(listOf(user!!))
      val backButton: MaterialTextView = findViewById(R.id.backButton)
      val recyclerContainer = findViewById<MaterialCardView>(R.id.recyclerContainer)

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

    viewAllMovies.setOnClickListener {
      val intent = Intent(this, GroupDetailsActivity::class.java)
      intent.putExtra("group", group)
      intent.putExtra("user", user)
      startActivity(intent)
      finish()
    }
  }
}