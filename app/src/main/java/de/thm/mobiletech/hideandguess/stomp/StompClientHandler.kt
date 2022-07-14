package de.thm.mobiletech.hideandguess.stomp

import android.util.Log
import de.thm.mobiletech.hideandguess.rest.UserAuth
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient
import kotlin.properties.Delegates

/**
 * Handler for creating and managing the stomp connection to the websocket server.
 * */
object StompClientHandler {

    private const val URL = "http://raspberrypi.tq2o4aj1y6ubht9d.myfritz.net:8080/example-endpoint/"

    lateinit var mStompClient: StompClient
    private lateinit var auth: UserAuth
    private var lobbyId by Delegates.notNull<Int>()

    fun setup(auth: UserAuth, lobbyId: Int) {
        this.auth = auth
        this.lobbyId = lobbyId

        val httpHeaders: MutableMap<String, String> = mutableMapOf()
        httpHeaders["Authorization"] = this.auth.authToken

        // replace last slash of the url otherwise the websockets server will not recognize the request
        val url = URL.substring(0, URL.lastIndexOf('/'))
        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url, httpHeaders)

        connect()
    }

    private fun connect() {
        // Connect to the websocket
        mStompClient.connect()

        /* if (!mStompClient.isConnected) {
            Log.e("StompClientHandler", "Failed to connect to websocket")
            return
        } */

    }

    fun send(destination: String, data: String? = "") {
        if (!mStompClient.isConnected)
            throw IllegalStateException("StompClient not connected")

        // Add topic broadcast subscription
        mStompClient.topic("/topic/response/$lobbyId")
            // .subscribeOn(Schedulers.io())
            // .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> Log.i("StompClientHandler", result.payload) },
                { error ->
                    Log.e(
                        "StompClientHandler",
                        "Error subscribing to topic: ${error?.message}"
                    )
                }
            )

        mStompClient.send("$destination/$lobbyId", data)
            .compose(applySchedulers())
            .subscribe(
                { Log.d("StompClientHandler", "Send successful") },
                { error -> Log.e("StompClientHandler", "Error sending message: ${error?.message}") }
            )
    }

    private fun applySchedulers(): CompletableTransformer {
        return CompletableTransformer { upstream ->
            upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }
}