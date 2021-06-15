package com.socialbox.login.ui.login

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Observer
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
import com.google.android.material.textfield.TextInputEditText
import com.socialbox.R
import com.socialbox.R.drawable
import com.socialbox.R.string
import com.socialbox.group.ui.GroupActivity
import com.socialbox.login.data.model.User
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

  private val loginViewModel: LoginViewModel by viewModels()
  private lateinit var username: EditText
  private lateinit var password: TextInputEditText
  private lateinit var loginButton: MaterialButton
  private lateinit var appImage: ImageView
  private lateinit var googleLoginButton: SignInButton
  private lateinit var progressIndicator: CircularProgressIndicator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.login_activity_layout)

    val mGoogleSignInClient = googleSignInClient()

    username = findViewById(R.id.input_username)
    password = findViewById(R.id.input_password)
    loginButton = findViewById(R.id.btn_login)
    googleLoginButton = findViewById(R.id.sign_in_button)
    appImage = findViewById(R.id.app_image)
    progressIndicator = findViewById(R.id.loading_icon)

    darkModeConfigure()
    setUpObserver()
    setUpButtons(mGoogleSignInClient)
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
      .build()
    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    val account = GoogleSignIn.getLastSignedInAccount(this)

    account?.let {
      val user = User(id = it.id, userName = it.displayName, userEmail = it.email)
      val intent = Intent(this, GroupActivity::class.java)
      intent.putExtra("user", user)
      startActivity(intent)
    }
    return mGoogleSignInClient
  }

  private fun setUpObserver() {
    loginViewModel.loginFormState.observe(this@LoginActivity, Observer {
      val loginState = it ?: return@Observer
      loginButton.isEnabled = loginState.isDataValid

      loginState.apply {
        usernameError?.let {
          username.error = getString(usernameError)
        }
        passwordError?.let {
          password.error = getString(passwordError)
        }
      }
    })

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

  private fun setUpButtons(mGoogleSignInClient: GoogleSignInClient) {
    username.doOnTextChanged { text, _, _, _ ->
      loginViewModel.loginDataChanged(text.toString(), password.text.toString())
    }

    password.apply {
      doOnTextChanged { text, _, _, _ ->
        loginViewModel.loginDataChanged(username.text.toString(), text.toString())
      }

      setOnEditorActionListener { _, actionId, _ ->
        when (actionId) {
          EditorInfo.IME_ACTION_DONE -> {
            progressIndicator.visibility = View.VISIBLE
            Timber.i("User requested log in")
            loginViewModel.login(
              User(userEmail = username.text.toString(), userPassword = password.text.toString()))
          }
        }
        false
      }
    }

    loginButton.setOnClickListener {
      progressIndicator.visibility = View.VISIBLE
      Timber.i("User requested log in")
      loginButton.isEnabled = false
      Toast.makeText(this, "Signing in. Please Wait", Toast.LENGTH_SHORT).show()
      loginViewModel.login(
        User(userEmail = username.text.toString(), userPassword = password.text.toString())
      )
    }

    googleLoginButton.setOnClickListener {
      val signInIntent: Intent = mGoogleSignInClient.signInIntent
      startActivityForResult(signInIntent, 1)
    }
  }

  /**
   * To go back to the screen
   */
  override fun onBackPressed() {
    Timber.i("User chose to close the screen")
    super.onBackPressed()
    finish()
  }

  private fun updateUiWithUser(model: User) {
    val welcome = getString(R.string.welcome)
    val displayName = model.userEmail
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
    loginButton.isEnabled = true
    progressIndicator.visibility = View.GONE
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?,
  ) {
    super.onActivityResult(requestCode, resultCode, data)

    if (requestCode == 1) {
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      handleSignInResult(task)
    }
  }

  private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
    try {
      val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)

      val user = User(id = account?.id, userName = account?.displayName, userEmail = account?.email)
      loginViewModel.login(user)
      updateUiWithUser(user)
    } catch (e: ApiException) {
      Timber.e("signInResult:failed code=%s", e.statusCode)
      showLoginFailed(string.error_google_sign)
    }
  }
}