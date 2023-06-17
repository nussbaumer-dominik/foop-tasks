package at.ac.tuwien.foop

import at.ac.tuwien.foop.plugins.configureSockets
import at.ac.tuwien.foop.routes.socketEndpoint
import at.ac.tuwien.foop.util.GameBoardGenerator
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    // TODO: init with data as env variables or something like that
    //generates a game board and prints it
    /*val gameBoard = GameBoardGenerator.generateGameBoard(10, 40, 4, 2, 10)

    // TODO: save the GameBoard globally to access it in the socketEndpoint

    gameBoard.generateGrid()
    println(gameBoard)
    println("Winning Subway: " + gameBoard.winningSubway)
    gameBoard.printGrid()
    println("---------------------------------------------------------")
    while (!gameBoard.isWinningState()) {
        gameBoard.moveMice()
        gameBoard.printGrid()
        println("---------------------------------------------------------")
        Thread.sleep(1000)
    }
    println("Mice won!")*/
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
