package at.ac.tuwien.foop.routes

import at.ac.tuwien.foop.Game
import at.ac.tuwien.foop.common.AoopMessage
import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.GameState
import at.ac.tuwien.foop.common.domain.Player
import at.ac.tuwien.foop.common.domain.Position
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

fun Application.socketEndpoint(game: Game) {
    routing {
        webSocket("/ws") {
            val gameBoard = game.board
            val player = Player(
                color = "red", position = Position(game.board.columns / 2, game.board.rows / 2)
            )

            // Send initial info
            game.addPlayerSession(this)
            sendSerialized(GlobalMessage.MapUpdate(map = gameBoard) as AoopMessage)
            sendSerialized(PrivateMessage.SetupInfo(player = player) as AoopMessage)

            // start game if not running
            if (game.state == GameState.WAITING) {
                game.start()
            }

            launch {
                while (true) {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val text = frame.readText()
                        println("Received on server: $text")
                        val moveCommand = Json.decodeFromString(PrivateMessage.MoveCommand.serializer(), text)
                        println(moveCommand)
                        game.addMove(playerId = player.id, direction = moveCommand.direction)
                    }
                }
            }
        }
    }
}
