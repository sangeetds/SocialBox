package com.socialbox.group.ui

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.socialbox.R
import com.socialbox.R.layout
import com.socialbox.group.data.model.Group
import timber.log.Timber

class AddGroupDialog(
  private val viewModel: GroupViewModel,
  private val userId: String,
  private val groupIds: Set<String>
) : DialogFragment() {

  private var dialogView: View? = null

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return MaterialAlertDialogBuilder(requireContext(), theme).apply {
      dialogView = onCreateView(LayoutInflater.from(requireContext()), null, savedInstanceState)
      dialogView?.let { onViewCreated(it, savedInstanceState) }
      setView(dialogView)
    }.create()
  }

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)
    val inflate = inflater.inflate(layout.add_group_dialog, container, false)
    val createNewGroupButton = inflate.findViewById<MaterialButton>(R.id.add_movie)
    val groupName = inflate.findViewById<EditText>(R.id.group_name)
    createNewGroupButton.setOnClickListener {
      Timber.i("New Group $groupName formed")
      val group =
        Group(groupName = groupName.text.toString(), memberCount = 0, groupAdminId = userId)
      viewModel.addGroup(group)
      viewModel.getGroupsForUser(groupIds.toList())
      val intent = Intent(context, GroupDetailsActivity::class.java)
      intent.putExtra("groupId", group.groupId)
      intent.putExtra("userId", userId)
      startActivity(intent)
      dismiss()
    }
    return inflate
  }

  override fun getView(): View? {
    return dialogView
  }
}