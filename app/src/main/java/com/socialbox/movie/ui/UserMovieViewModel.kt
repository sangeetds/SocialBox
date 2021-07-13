package com.socialbox.movie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.movie.data.UserMovieRepository
import com.socialbox.movie.data.dto.UserMovieDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserMovieViewModel @Inject constructor(private val userMovieRepository: UserMovieRepository) :
  ViewModel() {

  private val _userMovies = MutableLiveData<List<UserMovieDTO>>()
  val userMovies: LiveData<List<UserMovieDTO>> = _userMovies

  fun getUserMovies(userId: Int) = viewModelScope.launch {
    Timber.i("Fetching movies for user: $userId")
    val result = userMovieRepository.getUserMovies(id = userId)
    if (result.isNotEmpty()) _userMovies.value = result
  }
}