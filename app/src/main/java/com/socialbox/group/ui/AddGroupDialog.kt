package com.socialbox.group.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.socialbox.R
import com.socialbox.R.layout
import com.socialbox.group.data.model.Group
import com.socialbox.login.data.model.User
import timber.log.Timber

class AddGroupDialog(
  private val viewModel: GroupViewModel,
  private val user: User,
) : DialogFragment() {

  private var dialogView: View? = null

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return MaterialAlertDialogBuilder(requireContext(), theme).apply {
      dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
      dialogView?.let { onViewCreated(it, savedInstanceState) }
      setView(dialogView)
    }.create()
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    val inflate = inflater.inflate(layout.dialog_add_group, container, false)
    val createNewGroupButton = inflate.findViewById<MaterialButton>(R.id.add_movie)
    val groupName = inflate.findViewById<EditText>(R.id.group_name)

    createNewGroupButton.setOnClickListener {
      val group =
        Group(name = groupName.text.toString(), memberCount = 1, adminId = user.id!!)
      viewModel.addGroup(group)
      viewModel.groupState.observe(this@AddGroupDialog, Observer {
        val newGroup = it ?: return@Observer

        Timber.i("Adding Group ${newGroup.name} with id: ${newGroup.id} and userId: ${user.id}")
        user.groupsId.add(newGroup.id!!)
        viewModel.getGroupsForUser(user.groupsId.toList())
        val intent = Intent(context, GroupDetailsActivity::class.java)
        intent.putExtra("group", newGroup)
        intent.putExtra("user", user)
        startActivity(intent)
        dismiss()
      })
      Toast.makeText(context, "Adding Group. Please wait.", Toast.LENGTH_SHORT).show()
    }

    return inflate
  }

  override fun getView(): View? {
    return dialogView
  }
}