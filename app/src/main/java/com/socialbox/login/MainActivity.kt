package com.socialbox.login

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.socialbox.WelcomeScreen
import com.socialbox.login.ui.theme.SocialBoxTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    setContent {
      SocialBoxTheme {
        Surface(color = MaterialTheme.colors.background) {
          WelcomeScreen()
        }
      }
    }
  }
}