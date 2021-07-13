package com.socialbox.chat.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.socialbox.R
import com.socialbox.R.id
import com.socialbox.R.string
import com.socialbox.chat.data.model.Message
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.data.model.Group
import com.socialbox.login.data.model.User
import java.util.Calendar

class ChatActivity : AppCompatActivity() {

  private var messages: List<Message> = listOf()
  private val groupDTO by lazy { intent.getParcelableExtra<GroupDTO>("groupDTO") }
  private val group by lazy { intent.getParcelableExtra<Group>("group") }
  private val user by lazy { intent.getParcelableExtra<User>("user") }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat)
    val toolbar = findViewById<MaterialToolbar>(id.chatNameBar)
    toolbar.title = group?.name ?: groupDTO?.name ?: getString(string.appName)
    setSupportActionBar(toolbar)
    supportActionBar?.setDisplayShowTitleEnabled(true)

    val recyclerView: RecyclerView = findViewById(id.recyclerChat)
    val messageInput: EditText = findViewById(id.messageInput)
    val messageSendFab: FloatingActionButton = findViewById(id.sendButton)

    val adapter = MessageAdapter(user?.id, user?.photoURL)
    recyclerView.adapter = adapter
    recyclerView.layoutManager = LinearLayoutManager(this)
    recyclerView.setHasFixedSize(true)

    messageSendFab.setOnClickListener {
      val date = Calendar.getInstance().time.toString().split(" ")
      if (messageInput.text.isNotBlank()) {
        val message = Message(
          senderId = user?.id!!,
          content = messageInput.text.toString().trim(),
          createdAt = "${date[1]} ${date[2]} ${date[3]}",
          senderName = user?.name!!
        )
        val newMessages = messages + listOf(message)
        messages = newMessages
        adapter.submitList(newMessages)
        messageInput.text.clear()
      }
    }

    toolbar.setOnClickListener {
      val intent = Intent(this, ChatSettingsActivity::class.java)
      intent.putExtra("group", group)
      intent.putExtra("user", user)
      startActivity(intent)
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    return super.onCreateOptionsMenu(menu)
  }
}