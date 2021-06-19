package com.socialbox.group.data

import com.socialbox.group.data.model.Group
import com.socialbox.group.data.model.GroupMovie
import com.socialbox.group.data.service.GroupService
import com.socialbox.login.data.Result.Error
import com.socialbox.login.data.Result.Success
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import timber.log.Timber
import java.net.SocketTimeoutException
import javax.inject.Inject

class GroupRepository @Inject constructor(private val groupService: GroupService) {

  private val errorString = "Error while loading groups"

  suspend fun getGroupsForUser(groupId: List<String>) =
    try {
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

  suspend fun createGroup(group: Group) = groupService.saveGroup(group)

  suspend fun getGroup(groupId: String) =
    try {
      groupService.getGroup(groupId).run {
        if (isSuccessful && body() != null) {
          Success(body()!!)
        } else {
          Error(Exception(errorBody()?.stringSuspending()))
        }
      }
    } catch (exception: SocketTimeoutException) {
      Error(Exception("Error fetching group details."))
    }

  suspend fun saveGroupMovies(groupMovies: List<GroupMovie>) = groupService.saveGroupMovies(groupMovies)

  @Suppress("BlockingMethodInNonBlockingContext")
  private suspend fun ResponseBody.stringSuspending() =
    withContext(Dispatchers.IO) { string() }
}
