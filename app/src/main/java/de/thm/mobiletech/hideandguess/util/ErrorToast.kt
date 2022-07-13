package de.thm.mobiletech.hideandguess.util

import android.content.Context
import android.util.Log
import android.widget.Toast

fun Context.showError(tag: String, message: String, e: Throwable? = null) {
    if (e != null) {
        Toast.makeText(this, "$message: ${e.message}", Toast.LENGTH_SHORT).show()
        Log.e(tag, message, e)
    } else {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        Log.e(tag, message)
    }
}