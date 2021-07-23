package com.socialbox.group.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.common.enums.Result.Created
import com.socialbox.common.enums.Result.Error
import com.socialbox.common.enums.Result.Success
import com.socialbox.common.enums.ResultData
import com.socialbox.group.data.GroupRepository
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.GroupMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(private val groupRepository: GroupRepository) :
  ViewModel() {

  private val cachedGroups: MutableList<Group> = mutableListOf()
  private val _groupListState = MutableLiveData<ResultData<List<Group>>>()
  val groupListState: LiveData<ResultData<List<Group>>> = _groupListState

  private val _groupState = MutableLiveData<Group>()
  val groupState: LiveData<Group> = _groupState

  fun getGroupsForUser(groupId: List<Int>) = viewModelScope.launch {
    val groups = groupRepository.getGroupsForUser(groupId)
    _groupListState.value = when (groups) {
      is Success -> {
        cachedGroups.clear()
        cachedGroups.addAll(groups.data)
        ResultData(success = groups.data)
      }
      is Created -> {
        ResultData(created = groups.data)
      }
      is Error -> {
        cachedGroups.clear()
        ResultData(success = listOf(), error = groups.exception.localizedMessage)
      }
    }
  }

  fun addGroup(group: Group) = viewModelScope.launch {
    Timber.i("Saving group for admin: ${group.adminId}")
    val result = groupRepository.createGroup(group)
    if (result is Success) {
      _groupState.value = result.data
    }
  }

  fun getGroup(groupId: Int?) = viewModelScope.launch {
    Timber.i("Fetching details about the group $groupId")
    val result = groupRepository.getGroup(groupId!!)
    if (result is Success) {
      _groupState.value = result.data
    }
  }

  fun filterGroups(text: CharSequence) {
    _groupListState.value!!.success = cachedGroups.filter { it.name!!.contains(text) }
  }

  fun restoreGroups() {
    _groupListState.value!!.success = cachedGroups
  }

  fun addMovies(groupMovies: List<GroupMovie>) = viewModelScope.launch {
    groupRepository.saveGroupMovies(groupMovies)
  }

  fun addUserToGroup(groupId: Int, id: Int?) = viewModelScope.launch {
    val result = groupRepository.addUserToGroup(groupId, userId = id)
    if (result is Created) {
      _groupListState.value!!.success = _groupListState.value!!.success!! + result.data
    }
  }
}
