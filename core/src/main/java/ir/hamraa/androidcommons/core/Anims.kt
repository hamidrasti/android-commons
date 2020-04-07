package ir.hamraa.androidcommons.core

import android.animation.ValueAnimator
import android.view.View
import android.view.animation.OvershootInterpolator

object Anims {

    fun heartbeat(view: View, duration: Long) {
        view.scaleX = 0.1f
        view.scaleY = 0.1f
        val animator = ValueAnimator.ofFloat(0f, 1f).setDuration(duration)
        animator.addUpdateListener { animation: ValueAnimator ->
            val animatedFraction = animation.animatedFraction
            view.scaleX = 0.1f + animatedFraction * 0.9f
            view.scaleY = 0.1f + animatedFraction * 0.9f
        }
        animator.interpolator = OvershootInterpolator(2.0f)
        //animator.setStartDelay((long) (duration * 0.30f));
        animator.start()
    }

}