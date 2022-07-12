package de.thm.mobiletech.hideandguess.stomp

import android.util.Log
import io.reactivex.functions.Consumer
import ua.naiksoftware.stomp.dto.LifecycleEvent
import ua.naiksoftware.stomp.dto.LifecycleEvent.Type.*

/**
 * Callback handler for stomp lifecycle events.
 * Primarily used for logging.
 * */
class StompLifecycleHandler : Consumer<LifecycleEvent> {
    private val tag = "StompLifecycleHandler"

    override fun accept(lifecycleEvent: LifecycleEvent?) {
        when (lifecycleEvent?.type) {
            OPENED -> Log.d(tag, "Stomp connection opened")
            CLOSED -> Log.d(tag, "Stomp connection closed")
            ERROR -> Log.e(tag, "Stomp connection error: ${lifecycleEvent.exception.message}")
            FAILED_SERVER_HEARTBEAT -> Log.e(tag, "Stomp connection failed server heartbeat")
            else -> Log.d(tag, "Stomp lifecycle event: ${lifecycleEvent?.message}")
        }
    }
}