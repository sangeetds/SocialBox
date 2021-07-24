package com.socialbox.group.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.R.layout
import com.socialbox.group.data.model.Group
import com.socialbox.login.data.model.User
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

private const val ARG_PARAM1 = "param1"

@AndroidEntryPoint
class AddGroupDialog : com.socialbox.group.ui.BottomSheetDialog() {

  private val groupViewModel: GroupViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      user = it.getParcelable(ARG_PARAM1)!!
    }
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)

    return inflater.inflate(layout.bottom_dialog_add_group, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setUpViews(view)
  }

  private fun setUpViews(view: View) {
    val createNewGroupButton = view.findViewById<MaterialTextView>(R.id.addGroup)
    val groupName = view.findViewById<EditText>(R.id.group_name)

    createNewGroupButton.setOnClickListener {
      val group =
        Group(name = groupName.text.toString(), memberCount = 1, adminId = user.id!!)
      groupViewModel.addGroup(group)
      groupViewModel.groupState.observe(this@AddGroupDialog, Observer {
        val newGroup = it ?: return@Observer

        Timber.i("Adding Group ${newGroup.name} with id: ${newGroup.id} and userId: ${user.id}")
        user.groups.add(newGroup)
        groupViewModel.getGroupsForUser(user.groups.map { g -> g.id!! })
        val intent = GroupDetailsActivity.createIntent(requireContext(), newGroup, user)
        startActivity(intent)
        dismiss()
      })
      Toast.makeText(context, "Adding Group. Please wait.", Toast.LENGTH_SHORT).show()
    }
  }

  companion object {
    /**
     * @return A new instance of fragment AddGroupDialog.
     */
    @JvmStatic fun newInstance(param1: User) =
      AddGroupDialog().apply {
        arguments = Bundle().apply {
          putParcelable(ARG_PARAM1, param1)
        }
      }
  }
}