package de.thm.mobiletech.hideandguess

import android.app.AlertDialog
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import de.thm.mobiletech.hideandguess.util.TimerFinishedListener
import java.util.*

class MainActivity : AppCompatActivity() {

    var dialog: AlertDialog? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById(R.id.progressBarTimer)
    }

    fun startTimer(time: Int, callback: TimerFinishedListener) {
        if (!progressBar!!.isVisible)
            showTimer()

        progressBar?.max = time
        progressBar?.progress = 0
        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    progressBar?.progress = progressBar?.progress!! + 1
                    if (progressBar?.progress!! >= progressBar?.max!!) {
                        callback.onTimerFinished()
                        stopTimer()
                    }
                }
            }
        }, 0, 1000)
    }

    fun TimerTask.stopTimer() {
        cancel()
        hideTimer()
    }

    private fun showTimer() {
        progressBar!!.isVisible = true
    }

    private fun hideTimer() {
        progressBar!!.isInvisible = true
    }
}