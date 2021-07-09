package com.socialbox.group.ui

import android.app.Dialog
import android.content.Context
import android.content.res.Resources
import android.content.res.Resources.Theme
import android.os.Bundle
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.socialbox.R

class NotificationsDialog(context: Context) : ConstraintLayout(context) {

  init {
    init()
  }

  private fun init() {
    View.inflate(context, R.layout.dialog_notifications, this)
  }
}