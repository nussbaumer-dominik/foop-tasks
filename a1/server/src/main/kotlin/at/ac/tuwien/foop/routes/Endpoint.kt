package at.ac.tuwien.foop.routes

import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.common.domain.*
import at.ac.tuwien.foop.common.domain.Map
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.delay

fun Application.socketEndpoint() {
    routing {
        webSocket("/ws") {
            while (true) {
                delay(2000)
                sendSerialized(
                    GlobalMessage.MapUpdate(
                        map = Map(
                            subways = setOf(
                                Subway(
                                    position = Position(0, 0),
                                    size = Size(100, 100),
                                    exits = setOf(
                                        Exit(
                                            position = Position(100, 10),
                                            width = 20,
                                        ),
                                        Exit(
                                            position = Position(100, 90),
                                            width = 20,
                                        ),
                                    ),
                                ),
                                Subway(
                                    position = Position(200, 200),
                                    size = Size(200, 50),
                                    exits = setOf(
                                        Exit(
                                            position = Position(400, 210),
                                            width = 20,
                                        ),
                                    ),
                                ),
                            ),
                        ),
                    )
                )
            }
        }
    }
}
