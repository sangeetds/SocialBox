package com.socialbox.group.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.group.data.GroupRepository
import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.login.data.Result.Success
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GroupViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {

  private val _groupState = MutableLiveData<List<GroupDTO>>()
  val groupState: LiveData<List<GroupDTO>> = _groupState

  fun getGroupsForUser(userId: String) = viewModelScope.launch {
    groupRepository.getGroupsForUser(userId).collect { groups ->
      _groupState.value =
        if (groups is Success) groups.data
        else listOf()
    }
  }
}
