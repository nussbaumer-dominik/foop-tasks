package at.ac.tuwien.foop.util

import at.ac.tuwien.foop.domain.*
import at.ac.tuwien.foop.domain.mouseStrategy.MouseRandomStrategy
import at.ac.tuwien.foop.game.GameConfiguration

class GameBoardGenerator {
    companion object {
        fun generateGameBoard(gameConfiguration: GameConfiguration): GameBoard {
            val gameBoard = GameBoard(width = gameConfiguration.width, height = gameConfiguration.height)
            for (i in 0 until gameConfiguration.numberOfSubways) {
                val numberOfExits = (2..gameConfiguration.maxNumberOfExits).random()
                val subway = Subway()
                gameBoard.addSubway(subway)
                for (j in 0 until numberOfExits) {
                    // generate exits as long as they are not on the same position
                    while (true) {
                        val exit = Exit(
                            position = Position(
                                x = (0 until gameBoard.width - gameConfiguration.fieldSize + 1).random(),
                                y = (0 until gameBoard.height - gameConfiguration.fieldSize + 1).random(),
                                subwayId = subway.id
                            ),
                            subwayId = subway.id
                        )

                        if (gameBoard.isFieldEmpty(exit) && subway.addExit(exit))
                            break
                    }
                }
            }

            gameBoard.selectWinningSubway()
            placeMiceOnGameBoard(gameBoard, gameConfiguration.numberOfMice)
            return gameBoard
        }

        private fun placeMiceOnGameBoard(gameBoard: GameBoard, numberOfMice: Int) {
            val subwayExitPairs = gameBoard.getSubwayExitPairs().toMutableSet()
            //remove the winning subway so no mice is directly at the winning subway
            subwayExitPairs.removeIf { it.first == gameBoard.winningSubway }
            for (i in 0 until numberOfMice) {
                val subwayExitPair = subwayExitPairs.random()
                val mouse = Mouse(
                    position = subwayExitPair.second.position.copy(),
                    subway = subwayExitPair.first,
                    strategy = MouseRandomStrategy(),
                )
                gameBoard.mice.add(mouse)
            }
        }
    }
}
