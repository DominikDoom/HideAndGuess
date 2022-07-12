package de.thm.mobiletech.hideandguess.stomp

import android.util.Log
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableList
import io.reactivex.CompletableTransformer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import ua.naiksoftware.stomp.Stomp
import ua.naiksoftware.stomp.StompClient

/**
 * Handler for creating and managing the stomp connection to the websocket server.
 * @param auth The authentication token to send with every request. Identifies the user.
 *        Can be created with [StompAuth.encode]
 * */
class StompClientHandler(auth: StompAuth) {

    private val url = "http://192.168.178.42:8080/example-endpoint"
    private val mStompClient: StompClient
    private val mComposite: CompositeDisposable = CompositeDisposable()

    /**
     * The ObservableList of all messages broadcast on the topic.
     *  Observe it to react to new messages in the fragment / activity.
     * */
    val messages: ObservableList<String> = ObservableArrayList()

    init {
        val httpHeaders: MutableMap<String, String> = mutableMapOf()
        httpHeaders["Authorization"] = auth.authToken

        mStompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url, httpHeaders)
    }

    fun connect() {
        // Reset composite disposable to dispose old subscriptions.
        mComposite.clear()
        // Clear old messages
        messages.clear()

        // Add lifecycle subscription
        val dispLifecycle = mStompClient.lifecycle()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(StompLifecycleHandler())
        mComposite.add(dispLifecycle)

        // Add topic broadcast subscription
        val dispTopic = mStompClient.topic("/topic/greetings")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { result -> messages.add(result.payload) },
                { error -> Log.e("StompClientHandler", "Error subscribing to topic: ${error?.message}") }
            )
        mComposite.add(dispTopic)

        // Connect to the websocket
        mStompClient.connect()
    }

    fun disconnect() {
        mStompClient.disconnect()
        mComposite.clear()
        messages.clear()
    }

    fun send(destination: String, data: String) {
        if (!mStompClient.isConnected)
            throw IllegalStateException("StompClient not connected")

        val dispSend = mStompClient.send(destination, data)
            .compose(applySchedulers())
            .subscribe(
                { Log.d("StompClientHandler", "Send successful") },
                { error -> Log.e("StompClientHandler", "Error sending message: ${error?.message}") }
            )
        mComposite.add(dispSend)
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