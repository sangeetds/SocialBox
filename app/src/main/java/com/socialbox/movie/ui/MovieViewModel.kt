package com.socialbox.movie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.socialbox.group.data.MovieRepository
import com.socialbox.group.data.model.Movie
import com.socialbox.login.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
  private val movieRepository: MovieRepository,
  private val userRepository: UserRepository
) : ViewModel() {

  // Todo: Fetch latest Movies
  private val _latestMovies = liveData {
    emit(movieRepository.getAllMovies())
  }
  val latestMovies: LiveData<List<Movie>> = _latestMovies

  private val _userMovies = MutableLiveData<List<Movie>>()
  val userMovie: LiveData<List<Movie>> = _userMovies

  fun getUserMovies(userId: String) = viewModelScope.launch {
    val userMovies = userRepository.getAllMovies(userId)
    if (userMovies is com.socialbox.Result.Success) {
      _userMovies.value = userMovies.data.map {
        Movie(
          id = it.id,
          name = it.name,
          rating = it.rating,
          reviews = listOf(),
          photoURL = it.photoURL
        )
      }
    }
  }

  fun updateRatings(movie: Movie) = viewModelScope.launch {
    movieRepository.updateRatings(movie)
  }
}