package com.socialbox.group.data

import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.data.service.GroupService
import com.socialbox.login.data.Result
import com.socialbox.login.data.Result.Error
import com.socialbox.login.data.Result.Success
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class GroupRepository @Inject constructor(private val groupService: GroupService) {

  fun getGroupsForUser(userId: String) = flow {
    emit(getGroups(userId))
  }

  private suspend fun getGroups(userId: String) = try {
    groupService.getGroupsForUser(userId).run {
      if (isSuccessful && body() != null) {
        Success(body()!!)
      } else {
        Timber.e("Error while loading groups")
        Error(Exception("Erorr while loading groups"))
      }
    }
  } catch (exception: SocketTimeoutException) {
    Timber.e("Error while loading groups")
    Error(Exception("Erorr while loading groups"))
  }
}
