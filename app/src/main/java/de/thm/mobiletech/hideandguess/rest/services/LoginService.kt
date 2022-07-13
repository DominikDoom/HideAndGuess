package de.thm.mobiletech.hideandguess.rest.services

import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.UserAuth
import de.thm.mobiletech.hideandguess.rest.escape

private const val SECRET_KEY = "SUPER_SECRET_KEY_123456789"

suspend fun RestClient.login(username: String, password: String): Result<Int> {
    setAuth(UserAuth.encode(username, password))

    return try {
        postRequest("login")
    } catch (e: Exception) {
        Result.Error(e)
    }
}

suspend fun RestClient.register(username: String, password: String): Result<Int> {
    setAuth(UserAuth.encode(username, password))

    val body = """{
        "key": "$SECRET_KEY",
        "username":"${username.escape()}",
        "password": "${password.escape()}"
        }""".trimIndent().replace("\n", "")

    return try {
        putRequest("register", body)
    } catch (e: Exception) {
        Result.Error(e)
    }
}