package de.thm.mobiletech.hideandguess.stomp

import android.util.Base64
import android.util.Base64.encodeToString

data class StompAuth(val authToken: String) {
    companion object {
        @JvmStatic fun encode(username: String, password: String): StompAuth {
            return StompAuth("Basic " + encodeToString("$username:$password".toByteArray(), Base64.NO_WRAP))
        }
    }
}