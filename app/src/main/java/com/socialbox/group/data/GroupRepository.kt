package com.socialbox.group.data

import com.socialbox.common.enums.Result
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

  suspend fun getGroupsForUser(groupId: List<Int>) =
    try {
      Timber.i("Fetching groups for $groupId")
      groupService.getGroupsForUser(groupId).run {
        if (isSuccessful && body() != null) {
          Timber.i("Successfully fetched groups from server.")
          Success(body()!!)
        } else {
          Timber.e(errorBody()?.stringSuspending())
          Error(Exception(errorBody()?.stringSuspending()))
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.e(errorString)
      Error(Exception(errorString))
    }

  suspend fun createGroup(group: Group) = try {
    groupService.saveGroup(group).run {
      when {
        isSuccessful && body() != null -> {
          Timber.i("Successfully saved group ${group.name}")
          Success(body()!!)
        }
        else -> {
          Timber.d("Failed in saving group with error: ${errorBody()?.stringSuspending()}")
          Error(Exception(errorBody()?.stringSuspending()))
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
}
