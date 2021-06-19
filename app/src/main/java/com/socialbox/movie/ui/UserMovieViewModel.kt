package com.socialbox.movie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.group.data.GroupRepository
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.movie.data.UserMovieRepository
import com.socialbox.movie.data.dto.UserMovieDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserMovieViewModel @Inject constructor(private val userMovieRepository: UserMovieRepository, private val groupRepository: GroupRepository) : ViewModel() {

  private val _userMovies = MutableLiveData<List<UserMovieDTO>>()
  val userMovies: LiveData<List<UserMovieDTO>> = _userMovies

  fun getUserMovies(userId: String) = viewModelScope.launch {
    val result = userMovieRepository.getUserMovies(id = userId)
    if (result.isNotEmpty()) _userMovies.value = result
  }

  fun addMovies(groupMovies: List<GroupMovie>) = viewModelScope.launch {
    groupRepository.saveGroupMovies(groupMovies)
  }
}