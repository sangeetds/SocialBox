package com.socialbox.group.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.common.enums.Result.Success
import com.socialbox.group.data.GroupRepository
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.GroupMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(private val groupRepository: GroupRepository) :
  ViewModel() {

  private val cachedList: MutableList<GroupDTO> = mutableListOf()
  private val _groupListState = MutableLiveData<List<GroupDTO>>()
  val groupListState: LiveData<List<GroupDTO>> = _groupListState

  private val _groupState = MutableLiveData<Group>()
  val groupState: LiveData<Group> = _groupState

  fun getGroupsForUser(groupId: List<String>) = viewModelScope.launch {
    val groups = groupRepository.getGroupsForUser(groupId)
    _groupListState.value = when (groups) {
      is Success -> {
        cachedList.clear()
        cachedList.addAll(groups.data)
        groups.data
      }
      else -> {
        cachedList.clear()
        listOf()
      }
    }
  }

  fun addGroup(group: Group) = viewModelScope.launch {
    Timber.i("Saving group for admin: ${group.admin}")
    val result = groupRepository.createGroup(group)
    if (result is Success) {
      _groupState.value = result.data
    }
  }

  fun getGroup(groupId: String) = viewModelScope.launch {
    Timber.i("Fetching details about the group $groupId")
    val result = groupRepository.getGroup(groupId)
    if (result is Success) {
      _groupState.value = result.data
    }
  }

  fun filterGroups(text: CharSequence) {
    _groupListState.value = _groupListState.value?.filter { it.name.contains(text) }
  }

  fun restoreGroups() {
    _groupListState.value = cachedList
  }

  fun addMovies(groupMovies: List<GroupMovie>) = viewModelScope.launch {
    groupRepository.saveGroupMovies(groupMovies)
  }
}
