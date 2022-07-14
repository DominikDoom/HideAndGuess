package de.thm.mobiletech.hideandguess.rest.services

import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result

suspend fun RestClient.createLobby(): Result<Int> {
    return try {
        putRequest("lobby")
    } catch (e: Exception) {
        Result.Error(e)
    }
}