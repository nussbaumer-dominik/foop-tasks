package at.ac.tuwien.foop

import at.ac.tuwien.foop.common.util.GameBoardGenerator
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import at.ac.tuwien.foop.plugins.*
import at.ac.tuwien.foop.routes.socketEndpoint

fun main() {
    //generates a game board and prints it
    val gameBoard = GameBoardGenerator().generateGameBoard(10, 20, 10, 4, 10)
    gameBoard.print()
    println(gameBoard)
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
