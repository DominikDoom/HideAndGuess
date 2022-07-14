package de.thm.mobiletech.hideandguess.rest.services

import com.google.gson.Gson
import de.thm.mobiletech.hideandguess.Avatar
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result
import de.thm.mobiletech.hideandguess.rest.escape

suspend fun RestClient.getAvatar(): Result<String> {
    return try {
        getRequest("avatar")
    } catch (e: Exception) {
        Result.Error(e)
    }
}

suspend fun RestClient.postAvatar(avatar: Avatar): Result<Int> {

    val body = """{
        "indexHair": "${avatar.indexHair}",
        "indexFace":"${avatar.indexFace}",
        "indexClothes": "${avatar.indexClothes}"
        }""".trimIndent().replace("\n", "")

    return try {
        postRequest("avatar", body)
    } catch (e: Exception) {
        Result.Error(e)
    }
}