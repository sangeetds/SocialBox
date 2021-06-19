package com.socialbox.movie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.socialbox.group.data.MovieRepository
import com.socialbox.group.data.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
  private val movieRepository: MovieRepository,
  private val userRepository: MovieRepository
) : ViewModel() {

  // Todo: Fetch latest Movies
  private val _latestMovies = liveData {
    emit(movieRepository.getAllMovies())
  }
  val latestMovies: LiveData<List<Movie>> = _latestMovies

  private val _userMovies = liveData {
    emit(userRepository.getAllMovies())
  }
  val userMovie: LiveData<List<Movie>> = _userMovies
}