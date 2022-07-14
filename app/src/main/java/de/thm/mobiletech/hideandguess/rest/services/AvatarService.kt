package de.thm.mobiletech.hideandguess.rest.services

import com.google.gson.Gson
import de.thm.mobiletech.hideandguess.Avatar
import de.thm.mobiletech.hideandguess.rest.RestClient
import de.thm.mobiletech.hideandguess.rest.Result

suspend fun RestClient.getAvatar(): Result<String> {
    return try {
        getRequest("avatar")
    } catch (e: Exception) {
        Result.Error(e)
    }
}

suspend fun RestClient.postAvatar(avatar: Avatar): Result<Int> {
    println(Gson().toJson(avatar))
    return try {
        postRequest("avatar", Gson().toJson(avatar))
    } catch (e: Exception) {
        Result.Error(e)
    }
}