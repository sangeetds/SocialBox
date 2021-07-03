package com.socialbox.movie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.socialbox.common.enums.Result
import com.socialbox.group.data.MovieRepository
import com.socialbox.group.data.model.Movie
import com.socialbox.login.data.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
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

  private val _movies = MutableLiveData<List<Movie>>()
  val movies: LiveData<List<Movie>> = _movies

  fun getUserMovies(searchQuery: String) = viewModelScope.launch {
    val movies = movieRepository.searchMovie(searchQuery.trim())
    if (movies is Result.Success) {
      Timber.i("Search Successful loading ${movies.data.size} movies.")
      _movies.value = movies.data
    }
  }

  fun updateRatings(movie: Movie) = viewModelScope.launch {
    movieRepository.updateRatings(movie)
  }
}