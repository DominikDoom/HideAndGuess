package de.thm.mobiletech.hideandguess.rest

import android.util.Base64
import android.util.Base64.encodeToString

data class UserAuth(val authToken: String) {
    companion object {
        @JvmStatic fun encode(username: String, password: String): UserAuth {
            return UserAuth("Basic " + encodeToString("$username:$password".toByteArray(), Base64.NO_WRAP))
        }
    }
}