package at.ac.tuwien.foop.routes

import at.ac.tuwien.foop.common.GlobalMessage
import at.ac.tuwien.foop.util.GameBoardGenerator
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.delay

fun Application.socketEndpoint() {
    routing {
        webSocket("/ws") {
            //generates a game board and prints it
            val gameBoard = GameBoardGenerator.generateGameBoard(20, 20, 10, 4, 10)
            while (true) {
                delay(500)
                for (mouse in gameBoard.mice) {
                    mouse.move(gameBoard)
                }
                gameBoard.generateGrid()

                sendSerialized(
                    GlobalMessage.MapUpdate(
                        map = gameBoard,
                    ),
                )
            }
        }
    }
}
