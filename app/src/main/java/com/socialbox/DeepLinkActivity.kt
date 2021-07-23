package com.socialbox

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.deeplinkdispatch.DeepLinkHandler

@DeepLinkHandler(AppDeepLinkModule::class)
class DeepLinkActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val deepLinkDelegate = DeepLinkDelegate(AppDeepLinkModuleRegistry())
    deepLinkDelegate.dispatchFrom(this)
    finish()
  }
}