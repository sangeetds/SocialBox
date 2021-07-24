package com.socialbox.group.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textview.MaterialTextView
import com.socialbox.R
import com.socialbox.R.layout
import com.socialbox.group.data.model.Group
import com.socialbox.login.data.model.User
import timber.log.Timber

class AddGroupDialog(
  private val viewModel: GroupViewModel,
  private val user: User,
) : BottomSheetDialogFragment() {

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View? {
    super.onCreateView(inflater, container, savedInstanceState)

    return inflater.inflate(layout.bottom_dialog_add_group, container, false)
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = super.onCreateDialog(savedInstanceState)
    dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    dialog.setOnShowListener {
      Handler().post {
        val bottomSheet = (dialog as? BottomSheetDialog)?.findViewById<View>(R.id.design_bottom_sheet) as? FrameLayout
        bottomSheet?.let {
          BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
        }
      }
    }
    return dialog
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
      viewModel.addGroup(group)
      viewModel.groupState.observe(this@AddGroupDialog, Observer {
        val newGroup = it ?: return@Observer

        Timber.i("Adding Group ${newGroup.name} with id: ${newGroup.id} and userId: ${user.id}")
        user.groups.add(newGroup)
        viewModel.getGroupsForUser(user.groups.map { g -> g.id!! })
        val intent = GroupDetailsActivity.createIntent(requireContext(), newGroup, user)
        startActivity(intent)
        dismiss()
      })
      Toast.makeText(context, "Adding Group. Please wait.", Toast.LENGTH_SHORT).show()
    }
  }

  // companion object {
  //   fun newInstance(bundle: Bundle): AddGroupDialog {
  //     val fragment = AddGroupDialog()
  //     fragment.arguments = bundle
  //     return fragment
  //   }
  // }
}