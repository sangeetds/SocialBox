package com.socialbox.login.ui.login

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.socialbox.R
import com.socialbox.R.drawable
import com.socialbox.R.id
import com.socialbox.R.string
import com.socialbox.group.ui.GroupActivity
import com.socialbox.login.data.model.User
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

  private val loginViewModel: LoginViewModel by viewModels()
  private lateinit var appImage: ImageView
  private lateinit var googleLoginButton: SignInButton
  private lateinit var progressIndicator: CircularProgressIndicator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.login_activity_layout)

    googleLoginButton = findViewById(id.sign_in_button)
    appImage = findViewById(id.app_image)
    progressIndicator = findViewById(id.loading_icon)

    darkModeConfigure()
    setUpObserver()
    setUpButtons()
  }

  private fun darkModeConfigure() {
    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
      Configuration.UI_MODE_NIGHT_YES -> {
        Timber.i("Dark Mode Configured")
        Picasso.get().load(drawable.app_logo_dark).into(appImage)
      }
      Configuration.UI_MODE_NIGHT_NO -> {
        Timber.i("Day Mode Configured")
        Picasso.get().load(drawable.app_logo).into(appImage)
      }
    }
  }

  private fun googleSignInClient(): GoogleSignInClient {
    val gso = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .requestEmail()
      .build()
    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    val account = GoogleSignIn.getLastSignedInAccount(this)

    account?.let {
      Timber.i("User already signed in")
      val user = User(id = it.id, name = it.displayName, email = it.email)
      val intent = Intent(this, GroupActivity::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
    }
    return mGoogleSignInClient
  }

  private fun setUpObserver() {

    loginViewModel.loginResult.observe(this@LoginActivity, Observer {
      val loginResult = it ?: return@Observer
      Timber.i("Change in login result state.")

      loginResult.apply {
        error?.let {
          Timber.e("Login not successful")
          showLoginFailed(error)
        }
        success?.let {
          Timber.i("Successfully logged in")
          updateUiWithUser(success)
        }
        setResult(RESULT_OK)
      }
    })
  }

  private fun setUpButtons() {
    val mGoogleSignInClient = googleSignInClient()

    googleLoginButton.setOnClickListener {
      Timber.i("Signing the user through Google sign in.")
      val signInIntent: Intent = mGoogleSignInClient.signInIntent
      startActivityForResult(signInIntent, 1)
    }
  }

  override fun onBackPressed() {
    Timber.i("User chose to close the screen")
    super.onBackPressed()
    finish()
  }

  private fun updateUiWithUser(model: User) {
    val welcome = getString(R.string.welcome)
    val displayName = model.email
    Timber.i("Login successful for $model")

    Toast.makeText(applicationContext, "$welcome $displayName", Toast.LENGTH_LONG).show()

    Timber.i("Starting HomeScreenActivity")
    val songsActivity = Intent(this, GroupActivity::class.java)
    songsActivity.putExtra("user", model)
    startActivity(songsActivity)
    finish()
  }

  private fun showLoginFailed(@StringRes errorString: Int) {
    Timber.d("Logging failed.")
    Toast.makeText(applicationContext, errorString, Toast.LENGTH_LONG).show()
    progressIndicator.visibility = View.GONE
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?,
  ) {
    super.onActivityResult(requestCode, resultCode, data)

    when (requestCode) {
      1 -> {
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        handleSignInResult(task)
      }
    }
  }

  private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
    try {
      val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)

      val user = User(id = account?.id, name = account?.displayName, email = account?.email)
      loginViewModel.login(user)
      updateUiWithUser(user)
    } catch (e: ApiException) {
      Timber.e("signInResult:failed code=%s with message:%s", e.statusCode, e.message)
      showLoginFailed(string.error_google_sign)
    }
  }
}