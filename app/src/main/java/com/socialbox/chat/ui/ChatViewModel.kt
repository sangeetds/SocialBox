package com.socialbox.chat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.chat.data.model.Invite
import com.socialbox.common.enums.Result.Success
import com.socialbox.group.data.GroupRepository
import com.socialbox.login.data.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val groupRepository: GroupRepository) :
  ViewModel() {

  private val _membersState = MutableLiveData<List<User>>()
  val membersState: LiveData<List<User>> = _membersState

  private val _inviteState = MutableLiveData<Invite>()
  val inviteState: LiveData<Invite> = _inviteState

  fun getInviteLink(groupId: Int, userId: Int) = viewModelScope.launch {
    val result = groupRepository.getInviteLink(groupId, userId)
    if (result is Success) {
      _inviteState.value = result.data
    }
  }

  fun getMembers(groupId: Int) = viewModelScope.launch {
    val result = groupRepository.getUsers(groupId)
    if (result is Success) {
      _membersState.value = result.data
    }
  }
}
