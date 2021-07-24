package com.socialbox.user.ui

import android.content.Context
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
import com.socialbox.login.data.model.User
import com.socialbox.login.ui.LoginActivity
import timber.log.Timber

class UserActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_user_settings)

    val user = intent.getParcelableExtra<User>("user")
    val userWelcomeString = findViewById<TextView>(R.id.welcome_user)
    val logOutButton = findViewById<TextView>(R.id.logOut)
    userWelcomeString.text = userWelcomeString.text.toString().format(user?.name)

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
        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
      }
  }

  private fun getGoogleSignInClient(): GoogleSignInClient {
    val gso = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .build()
    return GoogleSignIn.getClient(this, gso)
  }

  companion object {

    private const val USER = "user"

    fun createIntent(context: Context, user: User?): Intent {
      val intent = Intent(context, UserActivity::class.java)
      intent.putExtra(USER, user)
      return intent
    }
  }
}