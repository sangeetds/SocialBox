package com.socialbox.login.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInOptions.Builder
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.socialbox.R
import com.socialbox.group.ui.GroupActivity
import com.socialbox.login.data.model.User
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

  private val loginViewModel: LoginViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val gso = Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
      .requestId()
      .requestEmail()
      .build()
    val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
    val account = GoogleSignIn.getLastSignedInAccount(this)

    account?.let {
      val user = User(id = it.id, userName = it.displayName, userEmail = it.email)
      val intent = Intent(this, GroupActivity::class.java)
      intent.putParcelableArrayListExtra("user", arrayListOf(user))
      startActivity(intent)
    }

    val loginFunction = {
      val signInIntent: Intent = mGoogleSignInClient.signInIntent
      startActivityForResult(signInIntent, 1)
    }


    loginViewModel.loginResult.observe(this@LoginActivity, Observer {
      val loginResult = it ?: return@Observer

      if (loginResult.error != null) {
        showLoginFailed(loginResult.error)
      }
      if (loginResult.success != null) {
        updateUiWithUser(loginResult.success)
      }
      setResult(Activity.RESULT_OK)

      //Complete and destroy login activity once successful
      finish()
    })
  }

  override fun onActivityResult(
    requestCode: Int,
    resultCode: Int,
    data: Intent?
  ) {
    super.onActivityResult(requestCode, resultCode, data)

    // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
    if (requestCode == 1) {
      // The Task returned from this call is always completed, no need to attach
      // a listener.
      val task = GoogleSignIn.getSignedInAccountFromIntent(data)
      handleSignInResult(task)
    }
  }

  private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
    try {
      val account = completedTask.getResult(ApiException::class.java)

      // Signed in successfully, show authenticated UI.
      val user = User(id = account?.id, userName = account?.displayName, userEmail = account?.email)
      val intent = Intent(this, GroupActivity::class.java)
      intent.putParcelableArrayListExtra("user", arrayListOf(user))
      startActivity(intent)
    } catch (e: ApiException) {
      Toast.makeText(this, "Please try again later.", Toast.LENGTH_LONG).show()
    }
  }

  private fun updateUiWithUser(model: User) {
    val welcome = getString(R.string.welcome)
    val displayName = model.userName
    Toast.makeText(
      applicationContext,
      "$welcome $displayName",
      Toast.LENGTH_LONG
    ).show()
    val user = User(id = model.id, userName = model.userName, userEmail = model.userEmail)
    val intent = Intent(this, GroupActivity::class.java)
    intent.putParcelableArrayListExtra("user", arrayListOf(user))
    startActivity(intent)
  }

  private fun showLoginFailed(@StringRes errorString: Int) {
    Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
  }
}