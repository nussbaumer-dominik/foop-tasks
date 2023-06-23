package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.exceptions.IllegalPositionException
import at.ac.tuwien.foop.domain.Exit
import at.ac.tuwien.foop.domain.GameBoard
import at.ac.tuwien.foop.domain.Mouse
import at.ac.tuwien.foop.domain.Position

class MouseRandomStrategy : MouseStrategy() {
    override fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position {
        while (true) {
            val direction = getRandomDirection()
            val newPosition = mouse.position.getNewPosition(direction)
            try {
                val field = gameBoard.getFieldAtPosition(newPosition)
                if (field is Exit) {
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