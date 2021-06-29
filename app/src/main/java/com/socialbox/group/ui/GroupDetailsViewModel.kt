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
class GroupDetailsViewModel @Inject constructor(private val movieRepository: MovieRepository) : ViewModel() {

  private val cachedList = mutableListOf<Movie>()
  private val _movieState = MutableLiveData<List<Movie>>()
  val movieState: LiveData<List<Movie>> = _movieState

  fun getMovies(groupId: String) = viewModelScope.launch {
    val movies = movieRepository.getMovies(groupId)
    Timber.i("Successfully fetched ${movies.joinToString(",") { m -> m.name }}")
    _movieState.value = movies
    cachedList.clear()
    cachedList.addAll(_movieState.value ?: listOf())
  }

  fun filterMovies(text: CharSequence) {
    _movieState.value = _movieState.value?.filter { it.name.contains(text) }
  }

  fun restoreMovies() {
    _movieState.value = cachedList
  }
}
