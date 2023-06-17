package at.ac.tuwien.foop.routes

import at.ac.tuwien.foop.common.AoopMessage
import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.common.PrivateMessage
import at.ac.tuwien.foop.common.domain.GameState
import at.ac.tuwien.foop.util.GameBoardGenerator
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

fun Application.socketEndpoint() {
    routing {
        webSocket("/ws") {
            //generates a game board and prints it
            val gameBoard = GameBoardGenerator.generateGameBoard(20, 20, 10, 4, 10)

            sendSerialized(
                GlobalMessage.MapUpdate(map = gameBoard) as AoopMessage,
            )

            // TODO: store playersession on first connection
            // TODO: send setup info to player on first connection
            while (true) {
                launch {
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val text = frame.readText()
                        println("Received: $text")
                        val moveCommand = Json.decodeFromString(PrivateMessage.MoveCommand.serializer(), text)
                        println(moveCommand)
                    }
                }

                delay(500)

                for (mouse in gameBoard.mice) {
                    mouse.move(gameBoard)
                }
                gameBoard.generateGrid()

                sendSerialized(
                    GlobalMessage.StateUpdate(map = gameBoard, state = GameState.RUNNING) as AoopMessage,
                )
            }
        }
    }
}
