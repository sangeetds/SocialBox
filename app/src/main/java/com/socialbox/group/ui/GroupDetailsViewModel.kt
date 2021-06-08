package com.socialbox.group.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.group.data.MovieRepository
import com.socialbox.group.data.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

  private val _movieState = MutableLiveData<List<Movie>>()
  val movieState: LiveData<List<Movie>> = _movieState

  fun getMovies(groupId: String) = viewModelScope.launch {
    movieRepository.getMovies(groupId).collect {
      _movieState.value = it
    }
  }
}
