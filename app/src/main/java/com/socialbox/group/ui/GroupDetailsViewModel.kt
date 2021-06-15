package com.socialbox.group.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.group.data.MovieRepository
import com.socialbox.group.data.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

  private val _movieState = MutableLiveData<List<Movie>>()
  val movieState: LiveData<List<Movie>> = _movieState

  fun getMovies(groupId: String) = viewModelScope.launch {
    _movieState.value = movieRepository.getMovies(groupId)
  }

  fun getAllMovies() = viewModelScope.launch {
    val allMovies = movieRepository.getAllMovies()
    Timber.i("Successfully fetched ${allMovies.joinToString(",") { m -> m.movieName }}")
    _movieState.value = allMovies
  }
}
