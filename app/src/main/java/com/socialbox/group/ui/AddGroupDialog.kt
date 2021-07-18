package com.socialbox.group.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
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
      // viewModel.addGroup(group)
      // viewModel.groupState.observe(this@AddGroupDialog, Observer {
      //   val newGroup = it ?: return@Observer

        Timber.i("Adding Group ${group.name} with id: ${group.id} and userId: ${user.id}")
        user.groups.add(group)
        // viewModel.getGroupsForUser(user.groups.map { g -> g.id!! })
        val intent = Intent(context, GroupDetailsActivity::class.java)
        intent.putExtra("group", group)
        intent.putExtra("user", user)
        startActivity(intent)
        dismiss()
      // })
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