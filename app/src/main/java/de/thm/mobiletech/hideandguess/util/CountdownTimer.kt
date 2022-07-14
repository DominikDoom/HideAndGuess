package de.thm.mobiletech.hideandguess.util

import android.os.CountDownTimer
import android.util.Log

object CountdownTimerThirtySeconds : CountDownTimer(30000, 1000) {

    override fun onTick(millisUntilFinished: Long) {
        Log.d("CountdownTimer", "onTick: $millisUntilFinished")
    }

    override fun onFinish() {
        Log.d("CountdownTimer", "onFinish")
    }
}
