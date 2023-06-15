package at.ac.tuwien.foop.common.util

import at.ac.tuwien.foop.common.domain.*

class GameBoardGenerator {
    fun generateGameBoard(rows: Int, columns: Int, numberOfSubways: Int, maxNumberOfExits: Int = 5, numberOfMice: Int): GameBoard {
        val gameBoard = GameBoard(rows = rows, columns = columns)
        for (i in 0 until  numberOfSubways) {
            val numberOfExits = (2..maxNumberOfExits).random()
            val subway = Subway()
            for (j in 0 until numberOfExits) {
                // generate exits as long as they are not on the same position
                while (true) {
                    val exit = Exit(
                        position = Position(
                            x = (0 until rows).random(),
                            y = (0 until columns).random(),
                        ),
                    )
                    if (gameBoard.isFieldEmpty(exit.position)) {
                        if (subway.addExit(exit)) {
                            break
                        }
                    }
                }
            }
            gameBoard.addSubway(subway)
        }
        gameBoard.selectWinningSubway()
        placeMiceOnGameBoard(gameBoard, numberOfMice)
        gameBoard.generateGrid()
        return gameBoard
    }

    private fun placeMiceOnGameBoard(gameBoard: GameBoard, numberOfMice: Int) {
        val exitPositions = gameBoard.getExitPositions()
        for (i in 0 until numberOfMice) {
            val mouse = Mouse(position = exitPositions.random(), inTube = true)
            gameBoard.mice.add(mouse)
        }
        gameBoard.generateGrid()
    }
}
