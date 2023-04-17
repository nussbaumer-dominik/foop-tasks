package at.ac.tuwien.foop

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import at.ac.tuwien.foop.plugins.*
import at.ac.tuwien.foop.routes.socketEndpoint

fun main() {
    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    configureSockets()
    socketEndpoint()
}
