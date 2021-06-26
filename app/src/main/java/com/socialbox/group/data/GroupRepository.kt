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

  private val errorString = "Error in connecting to the server"

  suspend fun getGroupsForUser(groupId: List<String>) =
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

  suspend fun createGroup(group: Group) = groupService.saveGroup(group)

  suspend fun getGroup(groupId: String) =
    try {
      Timber.i("Fetching group $groupId")
      groupService.getGroup(groupId).run {
        if (isSuccessful && body() != null) {
          Timber.i("Successfully fetched group: ${body()!!.id}")
          Success(body()!!)
        } else {
          Timber.d("Error in fetching group")
          Error(Exception(errorBody()?.stringSuspending()))
        }
      }
    } catch (exception: SocketTimeoutException) {
      Timber.d(errorString)
      Error(Exception("Error fetching group details."))
    }

  suspend fun saveGroupMovies(groupMovies: List<GroupMovie>) =
    groupService.saveGroupMovies(groupMovies)

  @Suppress("BlockingMethodInNonBlockingContext")
  private suspend fun ResponseBody.stringSuspending() =
    withContext(Dispatchers.IO) { string() }
}
