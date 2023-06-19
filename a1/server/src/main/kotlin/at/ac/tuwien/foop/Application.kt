package at.ac.tuwien.foop

import at.ac.tuwien.foop.common.domain.*
import at.ac.tuwien.foop.plugins.configureSockets
import at.ac.tuwien.foop.routes.socketEndpoint
import at.ac.tuwien.foop.util.MouseAlgorithms
import io.ktor.server.application.*

fun main() {
    //generates a game board and prints it
    /*val gameBoard = GameBoardGenerator.generateGameBoard(10, 40, 4, 2, 1)
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
    println("Mice won!")
     */
    val gameBoard = GameBoard(rows = 10, columns = 20)
    val subway1 = Subway()
    val subway2 = Subway()
    val subway3 = Subway()
    val s1e1 = Exit(Position(0, 0, subway1.id), subway1.id)
    val s1e2 = Exit(Position(19, 0, subway1.id), subway1.id)
    subway1.addExit(s1e1)
    subway1.addExit(s1e2)
    val s2e1 = Exit(Position(2, 1, subway2.id), subway2.id)
    val s2e2 = Exit(Position(9, 9, subway2.id), subway2.id)
    subway2.addExit(s2e1)
    subway2.addExit(s2e2)
    val s3e1 = Exit(Position(6, 6, subway3.id), subway3.id)
    val s3e2 = Exit(Position(7, 7, subway3.id), subway3.id)
    subway3.addExit(s3e1)
    subway3.addExit(s3e2)

    gameBoard.addSubway(subway1)
    gameBoard.addSubway(subway2)
    gameBoard.addSubway(subway3)

    gameBoard.winningSubway = subway3

    val mouse = Mouse(position = Position(0, 0, subway1.id), moveAlgorithm = MouseAlgorithms::moveLikeD)
    gameBoard.mice.add(mouse)
    gameBoard.generateGrid()
    println("Subway ways: ${gameBoard.getAllWays()}")
    println(gameBoard)
    println("Winning Subway: " + gameBoard.winningSubway)
    println("---------------------------------------------------------")
    gameBoard.printGrid()

    while (!gameBoard.isWinningState()) {
        println("---------------------------------------------------------")
        gameBoard.moveMice()
        gameBoard.printGrid()
        Thread.sleep(1000)
    }
    println("Mice won!")



    /*embeddedServer(
        factory = Netty,
        port = 8080,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)*/
}

fun Application.module() {
    configureSockets()
    socketEndpoint()
}
