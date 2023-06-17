package at.ac.tuwien.foop.util

import at.ac.tuwien.foop.common.domain.*


class GameBoardGenerator {
    companion object {
        fun generateGameBoard(
            rows: Int,
            columns: Int,
            numberOfSubways: Int,
            maxNumberOfExits: Int = 5,
            numberOfMice: Int
        ): GameBoard {
            val gameBoard = GameBoard(rows = rows, columns = columns)
            for (i in 0 until numberOfSubways) {
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
                            subwayId = subway.id
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
            placeCatsRandomlyOnGameBoard(gameBoard, 4)
            gameBoard.generateGrid()
            return gameBoard
        }

        private fun placeMiceOnGameBoard(gameBoard: GameBoard, numberOfMice: Int) {
            val subwayExitPairs = gameBoard.getSubwayExitPairs()
            for (i in 0 until numberOfMice) {
                val subwayExitPair = subwayExitPairs.random()
                val mouse = Mouse(
                    position = subwayExitPair.second.position.copy(),
                    subway = subwayExitPair.first,
                    moveAlgorithm = MouseAlgorithms::moveOptimal
                )
                gameBoard.mice.add(mouse)
            }
            gameBoard.generateGrid()
        }

        private fun placeCatsRandomlyOnGameBoard(gameBoard: GameBoard, numberOfCats: Int) {
            for (i in 0 until numberOfCats) {
                while (true) {
                    val cat = Player(
                        position = Position(
                            x = (0 until gameBoard.rows).random(),
                            y = (0 until gameBoard.columns).random(),
                        ),
                        color = "#"
                    )
                    if (gameBoard.isFieldEmpty(cat.position)) {
                        gameBoard.cats.add(cat)
                        break
                    }
                }
            }
        }
    }
}
