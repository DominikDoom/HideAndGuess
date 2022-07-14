package de.thm.mobiletech.hideandguess.rest.services

import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result

suspend fun RestClient.getStatistics(username: String): Result<String> {
    return try {
        getRequest("statistics/$username")
    } catch (e: Exception) {
        Result.Error(e)
    }
}

suspend fun RestClient.putStatistics(): Result<Int> {
    return try {
        putRequest("statistics")
    } catch (e: Exception) {
        Result.Error(e)
    }
}

data class Statistics(
    val type: Statistic,
    val value: Int
)

enum class Statistic {
    GUESSED_RIGHT,
    GUESSED_WRONG,
    POINTS_EARNED,
    ROUNDS_WON,
    ROUNDS_LOST,
    UNKNOWN
}