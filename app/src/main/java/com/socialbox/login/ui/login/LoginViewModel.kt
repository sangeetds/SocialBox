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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository) : ViewModel() {

  private val _loginForm = MutableLiveData<LoginFormState>()
  val loginFormState: LiveData<LoginFormState> = _loginForm

  private val _loginResult = MutableLiveData<LoginResult>()
  val loginResult: LiveData<LoginResult> = _loginResult

  fun login(user: User) = viewModelScope.launch {
    Timber.i("Making request to the server for user: $user")
    val result = loginRepository.login(user)
    _loginResult.value = if (result is Result.Success) {
      LoginResult(success = result.data)
    } else {
      LoginResult(error = R.string.login_failed)
    }
  }

  fun loginDataChanged(username: String, password: String) {
  _loginForm.value = if (!isEmail(username)) {
     LoginFormState(usernameError = R.string.invalid_username)
    } else if (!isPasswordValid(password)) {
      LoginFormState(passwordError = R.string.invalid_password)
    } else {
      LoginFormState(isDataValid = true)
    }
  }

  // A placeholder username validation check
  private fun isEmail(username: String) = Patterns.EMAIL_ADDRESS.matcher(username).matches()

  // A placeholder password validation check
  private fun isPasswordValid(password: String) = password.length > 5
}