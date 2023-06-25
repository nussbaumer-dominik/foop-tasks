package at.ac.tuwien.foop.routes

import at.ac.tuwien.foop.Game
import at.ac.tuwien.foop.common.domain.GameState
import at.ac.tuwien.foop.domain.Player
import at.ac.tuwien.foop.domain.Position
import at.ac.tuwien.foop.util.CommandListener
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch

fun Application.socketEndpoint(game: Game) {
    routing {
        webSocket("/ws") {
            val player = Player(
                color = "red",
                position = Position(game.board.width / 2 - (32 / 2), game.board.height / 2 - (32 / 2))
            )

            // Add player and send initial info(Player object and Map)
            game.addPlayerSession(this, player)

            // start listening for incoming messages in a separate coroutine
            val listener = launch {
                CommandListener(this@webSocket, game, player).start()
            }


            var gameRoutine: Job? = null
            // start game if not running
            if (game.state == GameState.WAITING) {
                gameRoutine = launch {
                    game.start()
                }

            }
            gameRoutine?.join()
            listener.cancelAndJoin()
        }
    }
}
