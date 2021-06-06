package com.socialbox.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.socialbox.R
import com.socialbox.login.ui.login.LoginActivity

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity_layout)

    val loginButton = findViewById<MaterialButton>(R.id.sign_in_button)
    loginButton.setOnClickListener {
      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
    }
  }
}