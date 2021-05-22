package com.socialbox

import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {

  lateinit var signUpButton: MaterialButton
  lateinit var signInButton: MaterialButton

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    this.supportActionBar?.hide()

    signInButton = findViewById(R.id.signInButton)
    signUpButton = findViewById(R.id.signUpButton)

    // signInButton.cornerRadius = 10
    // signInButton.strokeWidth = 10
    // signInButton.strokeColor = ColorStateList.valueOf(Color.BLACK)

    // signUpButton.cornerRadius = 10
    // signUpButton.strokeWidth = 10
    // signUpButton.strokeColor = ColorStateList.valueOf(Color.BLACK)

    signInButton.setOnClickListener {
      Toast.makeText(this, "Sign In Requested", Toast.LENGTH_SHORT).show()
    }
    signUpButton.setOnClickListener {
      Toast.makeText(this, "Sign Up Requested", Toast.LENGTH_SHORT).show()
    }
  }
}