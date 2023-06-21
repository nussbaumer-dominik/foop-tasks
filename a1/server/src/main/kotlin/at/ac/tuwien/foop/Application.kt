package at.ac.tuwien.foop

import at.ac.tuwien.foop.plugins.configureSockets
import at.ac.tuwien.foop.routes.socketEndpoint
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main() {
    //generates a game board and prints it
    /*val gameBoard = GameBoardGenerator.generateGameBoard(10, 40, 4, 2, 10)

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
    //generates a game board and prints it

    embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    val game = Game(configuration = GameConfiguration())
    configureSockets()
    socketEndpoint(game = game)
}
