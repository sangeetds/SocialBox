package com.socialbox.group.data

import com.socialbox.group.data.dto.GroupDTO
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.service.GroupService
import com.socialbox.login.data.Result
import com.socialbox.login.data.Result.Error
import com.socialbox.login.data.Result.Success
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class GroupRepository @Inject constructor(private val groupService: GroupService) {

  private val errorString = "Error while loading groups"

  fun getGroupsForUser(userId: String) = flow {
    emit(getGroups(userId))
  }

  private suspend fun getGroups(userId: String): Result<List<GroupDTO>> {
    return try {
      groupService.getGroupsForUser(userId).run {
        if (isSuccessful && body() != null) {
          Timber.i("Successfully fetched groups from server.")
          Success(body()!!)
        } else {
          Timber.e(errorString)
          Error(Exception(errorString))
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e(errorString)
      Error(Exception(errorString))
    }
  }

  suspend fun createGroup(group: Group) {
    groupService.saveGroup(group)
  }

  fun getGroup(groupId: String) = flow {
    emit(getGroupDetails(groupId))
  }

  private suspend fun getGroupDetails(groupId: String) = try {
    groupService.getGroup(groupId).run {
      if (isSuccessful && body() != null) {
        Success(body()!!)
      }
      else {
        Error(Exception(errorString))
      }
    }
  } catch (exception: SocketTimeoutException) {
    Error(Exception("Error fetching groups"))
  }
}
