package com.socialbox.user.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.socialbox.R
import com.socialbox.login.data.model.User

class UserActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user)

    val user = intent.getParcelableExtra<User>("user")
    val userWelcomeString = findViewById<TextView>(R.id.welcome_user)
    userWelcomeString.text = String.format(userWelcomeString.text.toString(), user?.userName)
  }
}