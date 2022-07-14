package de.thm.mobiletech.hideandguess.util

import android.app.AlertDialog
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import de.thm.mobiletech.hideandguess.MainActivity

fun MainActivity.showOtherPaintingDialog(painter: String) {
    val llPadding = 30
    val ll = LinearLayout(this)
    ll.orientation = LinearLayout.HORIZONTAL
    ll.setPadding(llPadding, llPadding, llPadding, llPadding)
    ll.gravity = Gravity.CENTER
    var llParam = LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.WRAP_CONTENT,
        LinearLayout.LayoutParams.WRAP_CONTENT
    )
    llParam.gravity = Gravity.CENTER
    ll.layoutParams = llParam

    llParam = LinearLayout.LayoutParams (ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
    llParam.gravity = Gravity.CENTER
    val tvText = TextView(this)
    tvText.text = "Please wait until $painter has finished painting the picture."
    tvText.setTextColor(Color.parseColor("#000000"))
    tvText.textSize = 20F
    tvText.layoutParams = llParam

    ll.addView(tvText)

    val builder = AlertDialog.Builder(this)
    builder.setCancelable(true)
    builder.setView(ll)

    dialog = builder.create()
    dialog!!.setCancelable(false)
    dialog!!.show()
    val window = dialog!!.window
    if (window != null) {
        val layoutParams = WindowManager.LayoutParams()
        layoutParams.copyFrom(dialog!!.window?.attributes ?: return)
        layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT
        dialog!!.window?.attributes = layoutParams
    }
}

fun MainActivity.hideOtherPaintingDialog() {
    dialog?.dismiss()
}