package at.ac.tuwien.foop.common.util

import at.ac.tuwien.foop.common.domain.*
import at.ac.tuwien.foop.common.exceptions.IllegalPositionException

class MouseAlgorithms {
    companion object {
        fun moveRandom(mouse: Mouse, gameBoard: GameBoard): Position {
            while (true) {
                val direction = getRandomDirection()
                val newPosition = mouse.position.getNewPosition(direction)
                try {
                    val field = gameBoard.getFieldAtPosition(newPosition)
                    if (field is EmptyField || field is Exit) {
                        return newPosition
                    }
                } catch (e: IllegalPositionException) {
                    continue
                }
            }
        }

        private fun getRandomDirection(): Direction {
            val directions = listOf(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
            return directions.random()
        }
    }


}
