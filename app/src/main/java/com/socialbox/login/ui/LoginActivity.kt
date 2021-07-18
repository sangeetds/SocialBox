package com.socialbox.login.ui

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.socialbox.R.drawable
import com.socialbox.R.id
import com.socialbox.R.layout
import com.socialbox.group.ui.GroupActivity
import com.socialbox.login.data.model.User
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

  private val loginViewModel: LoginViewModel by viewModels()
  private val appImage: ImageView by lazy { findViewById(id.appImage)}
  private val googleLoginButton: SignInButton by lazy { findViewById(id.googleSignInButton) }
  private val progressIndicator: CircularProgressIndicator by lazy { findViewById(id.loading_icon) }
  private val signInButton: MaterialButton by lazy { findViewById(id.signInButton) }
  private lateinit var mGoogleSignInClient: GoogleSignInClient

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_login)
    setUpObserver()
    darkModeConfigure()
    setUpViews()
    mGoogleSignInClient = googleSignInClient()
  }

  private fun darkModeConfigure() {
    when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
      Configuration.UI_MODE_NIGHT_YES -> {
        Picasso.get().load(drawable.app_logo_dark).into(appImage)
      }
      Configuration.UI_MODE_NIGHT_NO -> {
        Picasso.get().load(drawable.app_logo).into(appImage)
      }
    }
  }

  private fun googleSignInClient(): GoogleSignInClient {
    val gso = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .requestEmail()
      .requestProfile()
      .build()

    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    val account = GoogleSignIn.getLastSignedInAccount(this)

    account?.apply {
      progressIndicator.visibility = View.VISIBLE
      Toast.makeText(this@LoginActivity, "Logging in. Please Wait.", Toast.LENGTH_SHORT).show()
      val user = User(name = displayName, email = email)
      Timber.i("User $displayName already signed in")
      loginViewModel.login(user)
    }

    if (account == null) signInButton.isEnabled = true
    return mGoogleSignInClient
  }

  private fun setUpObserver() {
    loginViewModel.loginResult.observe(this@LoginActivity, Observer {
      val loginResult = it ?: return@Observer

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

  private fun setUpViews() {
    googleLoginButton.setOnClickListener {
      val signInIntent: Intent = mGoogleSignInClient.signInIntent
      startActivityForResult(signInIntent, 1)
      progressIndicator.visibility = View.VISIBLE
      Toast.makeText(this@LoginActivity, "Logging in. Please Wait.", Toast.LENGTH_SHORT).show()
    }

    signInButton.setOnClickListener {
      findViewById<ImageView>(id.appImage).animate().alpha(0.4f)

      val transition: Transition = Fade()
      transition.duration = 600
      transition.addTarget(id.loginView)
      transition.addTarget(id.signInButton)
      TransitionManager.beginDelayedTransition(findViewById(id.loginConstraintLayout), transition)

      it.visibility = View.GONE
      findViewById<ConstraintLayout>(id.loginView).visibility = View.VISIBLE
    }
  }

  override fun onBackPressed() {
    Timber.i("User chose to close the screen")
    super.onBackPressed()
    finish()
  }

  private fun updateUiWithUser(model: User) {
    Timber.i("Login successful for $model")

    Toast.makeText(applicationContext, "Welcome ${model.name}!", Toast.LENGTH_LONG).show()

    Timber.i("Starting HomeActivity with user: $model")
    val intent = Intent(this@LoginActivity, GroupActivity::class.java)
    intent.putExtra("user", model)
    startActivity(intent)
    finish()
  }

  private fun showLoginFailed(errorString: String) {
    Timber.d("Logging failed.")
    Toast.makeText(applicationContext, errorString, Toast.LENGTH_LONG).show()
    progressIndicator.visibility = View.GONE
    signInButton.isEnabled = true
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
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

      val user = User(name = account?.displayName, email = account?.email)
      loginViewModel.login(user)
    } catch (e: ApiException) {
      Timber.e("signInResult:failed code=%s with message:%s", e.statusCode, e.message)
      showLoginFailed(e.message ?: "Error Connecting to Google")
    }
  }
}