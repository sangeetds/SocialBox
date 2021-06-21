package com.socialbox.user.ui

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder
import com.socialbox.R
import com.socialbox.login.MainActivity
import com.socialbox.login.data.model.User
import timber.log.Timber

class UserActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.user_settings_layout)

    val user = intent.getParcelableExtra<User>("user")
    val userWelcomeString = findViewById<TextView>(R.id.welcome_user)
    val logOutButton = findViewById<TextView>(R.id.logOut)
    userWelcomeString.text = String.format(userWelcomeString.text.toString(), user?.name)

    logOutButton.setOnClickListener {
      signOut()
    }
  }

  private fun signOut() {
    val mGoogleSignInClient = getGoogleSignInClient()
    mGoogleSignInClient.signOut()
      .addOnCompleteListener(this) {
        Timber.i("Logging user out")
        Toast.makeText(this, "Successfully logged out.", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
      }
  }

  private fun getGoogleSignInClient(): GoogleSignInClient {
    val gso = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .build()
    return GoogleSignIn.getClient(this, gso)
  }
}