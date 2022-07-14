package de.thm.mobiletech.hideandguess.rest.services

import android.graphics.PointF
import com.google.gson.Gson
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.escape

suspend fun RestClient.submitPaintingChoice(query: String, lobbyId: Int): Result<Int> {
    try {
        return this.postRequest("imageChosen/$lobbyId/${query.escape()}")
    } catch (e: Exception) {
        throw Exception("Error while submitting painting choice")
    }
}

suspend fun RestClient.submitPainting(lobbyId: Int, point: ArrayList<PointF>): Result<Int> {
    try {
        return this.postRequest("submitPainting/$lobbyId", Gson().toJson(point).toString())
    } catch (e: Exception) {
        throw Exception("Error while submitting painting")
    }
}