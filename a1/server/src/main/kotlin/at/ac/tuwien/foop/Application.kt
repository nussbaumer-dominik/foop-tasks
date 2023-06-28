package at.ac.tuwien.foop

import at.ac.tuwien.foop.game.Game
import at.ac.tuwien.foop.game.GameConfiguration
import at.ac.tuwien.foop.game.GameImpl
import at.ac.tuwien.foop.plugins.configureRest
import at.ac.tuwien.foop.plugins.configureSocket
import at.ac.tuwien.foop.routes.restEndpoint
import at.ac.tuwien.foop.routes.socketEndpoint
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun main() {
    val applicationScope = CoroutineScope(Dispatchers.Default + Job())
    val game: Game = GameImpl(configuration = GameConfiguration())

    applicationScope.launch {
        game.startLoop()
    }

    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = { module(game) },
    ).start(wait = true)
}

fun Application.module(game: Game) {
    configureSocket()
    configureRest()
    socketEndpoint(game = game)
    restEndpoint(game = game)
}
