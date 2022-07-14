package de.thm.mobiletech.hideandguess.rest.services

import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result

suspend fun RestClient.create(): Result<String?> {
    return try {
        putRequestWithReturn("lobby")
    } catch (e: Exception) {
        Result.Error(e)
    }
}

suspend fun RestClient.lobbyInfo(lobbyId: Int): Result<String?> {
    return try {
        postRequestWithReturn("lobbyInfo/$lobbyId")
    } catch (e: Exception) {
        Result.Error(e)
    }
}

suspend fun RestClient.leaveLobby(): Result<Int> {
    return try {
        postRequest("leave")
    } catch (e: Exception) {
        Result.Error(e)
    }
}