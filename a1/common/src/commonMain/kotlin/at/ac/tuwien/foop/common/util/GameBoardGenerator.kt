package at.ac.tuwien.foop.common.util

import at.ac.tuwien.foop.common.domain.Exit
import at.ac.tuwien.foop.common.domain.GameBoard
import at.ac.tuwien.foop.common.domain.Position
import at.ac.tuwien.foop.common.domain.Subway

class GameBoardGenerator {
    fun generateGameBoard(rows: Int, columns: Int, numberOfSubways: Int, maxNumberOfExits: Int = 5): GameBoard {
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
                    if (gameBoard.isTileEmpty(exit.position)) {
                        if (subway.addExit(exit)) {
                            break
                        }
                    }
                }
            }
            gameBoard.addSubway(subway)
        }

        return gameBoard
    }
}
