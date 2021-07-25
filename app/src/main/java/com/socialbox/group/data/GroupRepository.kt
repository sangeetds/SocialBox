package com.socialbox.group.data

import com.socialbox.common.enums.Result
import com.socialbox.common.enums.Result.Created
import com.socialbox.common.enums.Result.Error
import com.socialbox.common.enums.Result.Success
import com.socialbox.common.util.RepositoryUtils.Companion.stringSuspending
import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.group.data.service.GroupService
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class GroupRepository @Inject constructor(private val groupService: GroupService) {

  private val errorString = "Error in connecting to the server"

  suspend fun createGroup(group: Group) = try {
    val saveGroup = groupService.saveGroup(group)
    saveGroup.run {
      when {
        isSuccessful && body() != null -> {
          Timber.i("Successfully saved group ${group.name}")
          Success(body()!!)
        }
        else -> {
          val errorMessage = errorBody()?.stringSuspending()
          Timber.d("Failed in saving group with error: $errorMessage")
          Error(Exception(errorMessage))
        }
      }
    }
  } catch (exception: SocketTimeoutException) {
    Timber.d(errorString)
    Error(Exception("Error fetching group details."))
  }

  suspend fun getGroup(groupId: Int): Result<Group> =
    try {
      Timber.i("Fetching group $groupId")
      groupService.getGroup(groupId).run {
        if (isSuccessful && body() != null) {
          Timber.i("Successfully fetched group: ${body()!!.id}")
          Success(body()!!)
        } else {
          Timber.d("Error in fetching group details")
          Error(Exception(errorBody()?.stringSuspending()))
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.d(errorString)
      Error(Exception("Error fetching group details."))
    }

  suspend fun saveGroupMovies(groupMovies: List<GroupMovie>) {
    groupService.saveGroupMovies(groupMovies).run {
      when {
        isSuccessful -> Timber.i("Successfully saved group movies")
        else -> Timber.d("Failed in saving group with error: ${errorBody()?.stringSuspending()}")
      }
    }
  }

  suspend fun getInviteLink(groupId: Int, userId: Int) = try {
    groupService.getInviteLink(groupId, userId).run {
      if (isSuccessful && body() != null) {
        Timber.i("Successfully fetched group invite link")
        Success(body()!!)
      } else {
        Timber.d("Error in fetching invite link")
        Error(Exception(errorBody()?.stringSuspending()))
      }
    }
  } catch (exception: SocketTimeoutException) {
    Timber.d(errorString)
    Error(Exception("Error fetching invite link."))
  }

  suspend fun addUserToGroup(groupId: Int, userId: Int?) = try {
    this.groupService.addUserToGroup(groupId, userId).run {
      if (isSuccessful && body() != null) {
        Timber.i("Successfully added User to Group.")
        Created(body()!!)
      } else {
        Timber.d("Error in adding user to the group.")
        Error(Exception(errorBody()?.stringSuspending()))
      }
    }
  } catch (exception: SocketTimeoutException) {
    Timber.d(errorString)
    Error(Exception("Error adding user."))
  }

  suspend fun getUsers(groupId: Int) = try {
    this.groupService.getUsers(groupId).run {
      if (isSuccessful && body() != null) {
        Timber.i("Successfully added User to Group.")
        Created(body()!!)
      } else {
        Timber.d("Error in adding user to the group.")
        Error(Exception(errorBody()?.stringSuspending()))
      }
    }
  } catch (exception: SocketTimeoutException) {
    Timber.d(errorString)
    Error(Exception("Error adding user."))
  }
}
