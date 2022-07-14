package de.thm.mobiletech.hideandguess

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.services.leaveLobby
import de.thm.mobiletech.hideandguess.rest.services.submitPaintingChoice
import de.thm.mobiletech.hideandguess.util.TimerFinishedListener
import de.thm.mobiletech.hideandguess.util.showError
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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

    override fun onPause() {
        // leave lobby
        lifecycleScope.launch {
            Log.d("MainActivity", "Leaving lobby")
            val defer = async { RestClient.leaveLobby() }
            defer.await()
        }
        super.onPause()
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