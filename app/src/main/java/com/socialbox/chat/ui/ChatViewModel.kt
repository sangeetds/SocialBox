package com.socialbox.chat.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socialbox.chat.data.model.Invite
import com.socialbox.common.enums.Result.Success
import com.socialbox.group.data.GroupRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val groupRepository: GroupRepository) : ViewModel() {

  private val _inviteState = MutableLiveData<Invite>()
  val inviteState: LiveData<Invite> = _inviteState

  fun getInviteLink(groupId: Int, userId: Int) = viewModelScope.launch {
    val result = groupRepository.getInviteLink(groupId, userId)
    if (result is Success) {
      _inviteState.value = result.data
    }
  }
}
