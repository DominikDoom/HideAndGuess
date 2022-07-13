package de.thm.mobiletech.hideandguess.stomp.services

import de.thm.mobiletech.hideandguess.rest.UserAuth
import de.thm.mobiletech.hideandguess.stomp.StompClientHandler

fun StompClientHandler.join(username: String, password: String, lobbyId: Int) {
    val auth = UserAuth.encode(username, password)
    setup(auth, lobbyId)

    if (mStompClient.isConnected)
        send("queue/join")
}

fun StompClientHandler.leave() {
    send("queue/leave")
}