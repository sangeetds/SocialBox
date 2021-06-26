package com.socialbox.group.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.group.data.GroupRepository
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.data.model.Group
import com.socialbox.Result.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {

  private val _groupListState = MutableLiveData<List<GroupDTO>>()
  val groupListState: LiveData<List<GroupDTO>> = _groupListState

  private val _groupState = MutableLiveData<Group>()
  val groupState: LiveData<Group> = _groupState

  fun getGroupsForUser(groupId: List<String>) = viewModelScope.launch {
    val groups = groupRepository.getGroupsForUser(groupId)
    _groupListState.value = when(groups) {
      is Success -> groups.data
      else -> listOf()
    }
  }

  fun addGroup(group: Group) = viewModelScope.launch {
    groupRepository.createGroup(group)
  }

  fun getGroup(groupId: String) = viewModelScope.launch {
    val result = groupRepository.getGroup(groupId)
    if (result is Success) {
      _groupState.value = result.data
    }
  }
}
