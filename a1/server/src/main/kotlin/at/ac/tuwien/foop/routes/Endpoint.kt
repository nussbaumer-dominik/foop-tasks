package at.ac.tuwien.foop.routes

import at.ac.tuwien.foop.common.Message
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay

fun Application.socketEndpoint() {
    routing {
        webSocket("/ws") {
            while (true) {
                delay(2000)
                sendSerialized(Message("Hey"))
            }
        }
    }
}
