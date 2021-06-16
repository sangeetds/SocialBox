package com.socialbox.movie.ui

import android.animation.AnimatorSet

import android.animation.AnimatorInflater
import android.content.Context

import android.view.View
import com.socialbox.R.animator

object FlipAnimator {

  /**
   * Performs flip animation on two views
   */
  fun flipView(context: Context?, back: View?, front: View?, showFront: Boolean) {
    val leftIn = AnimatorInflater.loadAnimator(context, animator.card_flip_in) as AnimatorSet
    val rightOut = AnimatorInflater.loadAnimator(context, animator.card_flip_right_out) as AnimatorSet
    val leftOut = AnimatorInflater.loadAnimator(context, animator.card_flip_out) as AnimatorSet
    val rightIn = AnimatorInflater.loadAnimator(context, animator.card_flip_right_in) as AnimatorSet
    val showFrontAnim = AnimatorSet()
    val showBackAnim = AnimatorSet()
    leftIn.setTarget(back)
    rightOut.setTarget(front)
    showFrontAnim.playTogether(leftIn, rightOut)
    leftOut.setTarget(back)
    rightIn.setTarget(front)
    showBackAnim.playTogether(rightIn, leftOut)
    if (showFront) {
      showFrontAnim.start()
    } else {
      showBackAnim.start()
    }
  }
}