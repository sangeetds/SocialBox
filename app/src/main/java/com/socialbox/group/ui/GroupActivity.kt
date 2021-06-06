package com.socialbox.group.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.socialbox.theme.SocialBoxTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GroupActivity : AppCompatActivity() {

  private val groupViewModel: GroupViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      SocialBoxTheme {
        Surface {
          GroupScreen(groupViewModel)
        }
      }
    }
  }

  @Preview
  @Composable
  fun Preview() {
    GroupScreen(groupViewModel)
  }

}


