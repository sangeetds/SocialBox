package com.socialbox.group.ui

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.view.ViewAnimationUtils

class AnimationUtils {

  companion object {
    fun View.circleReveal(isShow: Boolean) {
      val width = this.width
      val height = this.height / 2

      val anim = if (isShow)
        ViewAnimationUtils.createCircularReveal(this, width, height, 0f, width.toFloat())
      else
        ViewAnimationUtils.createCircularReveal(this, width, height, width.toFloat(), 0f)
      anim.duration = 220L

      anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
          if (!isShow) {
            super.onAnimationEnd(animation)
            this@circleReveal.visibility = View.INVISIBLE
          }
        }
      })

      // make the view visible and start the animation
      if (isShow) {
        this.visibility = View.VISIBLE
      }
      // start the animation
      anim.start()
    }
  }
}