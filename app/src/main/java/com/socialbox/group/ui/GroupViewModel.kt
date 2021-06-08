package com.socialbox.group.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.group.data.GroupRepository
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.data.model.Group
import com.socialbox.login.data.Result.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {

  private val _groupListState = MutableLiveData<List<GroupDTO>>()
  val groupListState: LiveData<List<GroupDTO>> = _groupListState

  private val _group = MutableLiveData<Group>()
  val group: LiveData<Group> = _group

  fun getGroupsForUser(userId: String) = viewModelScope.launch {
    groupRepository.getGroupsForUser(userId).collect { groups ->
      _groupListState.value =
        if (groups is Success) groups.data
        else listOf()
    }
  }

  fun addGroup(group: Group) = viewModelScope.launch {
    groupRepository.createGroup(group)
    getGroupsForUser(group.groupAdminId)
  }

  fun getGroup(groupId: String) = viewModelScope.launch {
    groupRepository.getGroup(groupId).collect { result ->
      if (result is Success) {
        _group.value = result.data
      }
    }
  }
}
