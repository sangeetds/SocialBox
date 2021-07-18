package com.socialbox.movie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.socialbox.common.enums.Genre
import com.socialbox.common.enums.Result
import com.socialbox.group.data.MovieRepository
import com.socialbox.group.data.model.Movie
import com.socialbox.login.data.UserRepository
import com.socialbox.movie.data.model.UserMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
  private val movieRepository: MovieRepository,
  private val userRepository: UserRepository
) : ViewModel() {

  private val _personalMovie = MutableLiveData<List<UserMovie>>()
  val personalMovie: LiveData<List<UserMovie>> = _personalMovie

  // Todo: Have sealed class with genre, so that different genres can be segregated with one live data
  private val _documentaryMovies = liveData {
    emit(movieRepository.getDocumentaryMovies())
  }
  val documentaryMovies: LiveData<Result<List<Movie>>> = _documentaryMovies

  private val _movies = MutableLiveData<List<Movie>>()
  val movies: LiveData<List<Movie>> = _movies

  fun searchAllMovies(searchQuery: String, genre: Genre? = null) = viewModelScope.launch {
    val movies = movieRepository.searchMovie(searchQuery.trim())
    if (movies is Result.Success) {
      Timber.i("Search Successful loading ${movies.data.size} movies.")
      _movies.value = movies.data
    }
  }

  fun updateRatings(movie: Movie) = viewModelScope.launch {
    movieRepository.updateRatings(movie)
  }

  fun getPersonalMovies(id: Int) = viewModelScope.launch {
    val movies1 = userRepository.getMovies(id)
    if (movies1 is Result.Success) {
      Timber.i("Search Successful loading ${movies1.data.size} movies.")
      _personalMovie.value = movies1.data
    }
  }
}