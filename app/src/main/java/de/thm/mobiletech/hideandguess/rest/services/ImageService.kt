package de.thm.mobiletech.hideandguess.rest.services

import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result

suspend fun RestClient.getImageOptions(): Result<String?> {
    return try {
        postRequestWithReturn("images")
    } catch (e: Exception) {
        Result.Error(e)
    }
}

suspend fun RestClient.getSynonyms(lobbyId: Int): Result<String> {
    return try {
        getRequest("synonyms/$lobbyId")
    } catch (e: Exception) {
        Result.Error(e)
    }
}