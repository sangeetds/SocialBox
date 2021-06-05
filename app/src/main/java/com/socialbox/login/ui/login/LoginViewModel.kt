package com.socialbox.login.ui.login

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.R
import com.socialbox.login.data.LoginRepository
import com.socialbox.login.data.Result
import com.socialbox.login.data.model.User
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

  private val _loginForm = MutableLiveData<LoginFormState>()
  val loginFormState: LiveData<LoginFormState> = _loginForm

  private val _loginResult = MutableLiveData<LoginResult>()
  val loginResult: LiveData<LoginResult> = _loginResult

  fun login(user: User) = viewModelScope.launch {
    Timber.i("Making request to the server for user: $user")
    loginRepository.login(user).collect { result ->
      if (result is Result.Success) {
        _loginResult.value =
          LoginResult(success = result.data)
      } else {
        _loginResult.value = LoginResult(error = R.string.login_failed)
      }
    }
  }

  fun loginDataChanged(username: String, password: String) {
    if (!isEmail(username)) {
      _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
    } else if (!isPasswordValid(password)) {
      _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
    } else {
      _loginForm.value = LoginFormState(isDataValid = true)
    }
  }

  // A placeholder username validation check
  private fun isEmail(username: String) = Patterns.EMAIL_ADDRESS.matcher(username).matches()

  // A placeholder password validation check
  private fun isPasswordValid(password: String) = password.length > 5
}