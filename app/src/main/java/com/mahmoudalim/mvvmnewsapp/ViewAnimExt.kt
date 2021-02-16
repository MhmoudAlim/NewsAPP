package com.mahmoudalim.mvvmnewsapp

import android.view.View
import android.view.animation.AnimationUtils
import androidx.interpolator.view.animation.FastOutLinearInInterpolator

fun View.slideOutLift(animTime: Long, startOffset: Long){
    val slideOutLift = AnimationUtils.loadAnimation(context, R.anim.slide_out_left).apply {
        duration = animTime
        interpolator = FastOutLinearInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideOutLift)
}

fun View.slideInRight(animTime: Long, startOffset: Long){
    val slideInRight = AnimationUtils.loadAnimation(context, R.anim.slide_in_right).apply {
        duration = animTime
        interpolator = FastOutLinearInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideInRight)
}

fun View.slideDown(animTime: Long, startOffset: Long){
    val slideDown = AnimationUtils.loadAnimation(context, R.anim.slide_down).apply {
        duration = animTime
        interpolator = FastOutLinearInInterpolator()
        this.startOffset = startOffset
    }
    startAnimation(slideDown)
}