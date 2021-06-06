package com.socialbox.login

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import com.google.android.material.button.MaterialButton
import com.socialbox.R
import com.socialbox.login.ui.login.LoginActivity
import com.squareup.picasso.Picasso
import timber.log.Timber

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.main_activity_layout)
    Timber.i("Main Screen set up.")

    val loginButton = findViewById<MaterialButton>(R.id.sign_in_button)
    loginButton.setOnClickListener {
      val intent = Intent(this, LoginActivity::class.java)
      startActivity(intent)
    }

    val appImage = findViewById<ImageView>(R.id.app_image)

    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
      Configuration.UI_MODE_NIGHT_YES -> Picasso.get().load(R.drawable.app_logo_dark).into(appImage)
      Configuration.UI_MODE_NIGHT_NO -> Picasso.get().load(R.drawable.app_logo).into(appImage)
    }
  }
}