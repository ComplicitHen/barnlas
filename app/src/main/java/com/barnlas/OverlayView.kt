package com.barnlas

import android.content.Context
import android.view.MotionEvent
import android.view.View

class OverlayView(context: Context) : View(context) {

    private var tapCount = 0
    private var lastTapTime = 0L
    private var unlockListener: (() -> Unit)? = null

    fun setOnUnlockListener(listener: () -> Unit) {
        unlockListener = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            val cornerPx = 150 * resources.displayMetrics.density
            val inCorner = event.x > width - cornerPx && event.y < cornerPx
            val now = System.currentTimeMillis()

            if (now - lastTapTime > 3000) tapCount = 0
            lastTapTime = now

            if (inCorner) {
                tapCount++
                if (tapCount >= 5) {
                    tapCount = 0
                    unlockListener?.invoke()
                }
            } else {
                tapCount = 0
            }
        }
        return true // blockera alla touch-events
    }
}
