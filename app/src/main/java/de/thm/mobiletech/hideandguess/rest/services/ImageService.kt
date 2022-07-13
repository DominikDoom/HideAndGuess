package de.thm.mobiletech.hideandguess.rest.services

import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result

suspend fun RestClient.getImageOptions(): Result<String> {
    return try {
        TODO("Get image options from REST backend")
    } catch (e: Exception) {
        Result.Error(e)
    }
}