package de.thm.mobiletech.hideandguess

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import java.util.*

class MainActivity : AppCompatActivity() {

    var dialog: AlertDialog? = null
    var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        progressBar = findViewById<ProgressBar>(R.id.progressBarTimer)
    }

    fun startTimer(time: Int) {
        progressBar?.max = time
        progressBar?.progress = 0
        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    progressBar?.progress = progressBar?.progress!! + 1
                    if (progressBar?.progress!! >= progressBar?.max!!) {
                        // SMTH HERE
                    }
                }
            }
        }, 0, 1000)
    }

    fun showTimer() {
        progressBar!!.isVisible = true
    }

    fun hideTimer() {
        progressBar!!.isInvisible = true
    }
}