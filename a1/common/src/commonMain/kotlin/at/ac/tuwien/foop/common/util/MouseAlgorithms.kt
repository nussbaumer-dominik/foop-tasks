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

        fun moveDirectlyTowardsClosestWinningExit(mouse: Mouse, gameBoard: GameBoard): Position {
            //val exitPositions = gameBoard.getExitPositions()
            val closestExit = gameBoard.winningSubway!!.exits.map { it.position }.minByOrNull { mouse.position.distanceTo(it) }
            //if mouse is already in winning subway -> do nothing
            if (closestExit == mouse.position) {
                mouse.inTube = true
                return mouse.position
            }
            val direction = getDirectionTowardsPosition(mouse.position, closestExit!!) ?: return mouse.position
            val newPosition = mouse.position.getNewPosition(direction)
            try {
                val field = gameBoard.getFieldAtPosition(newPosition)
                if (field !is Player) {
                    mouse.inTube = false
                    return newPosition
                }
            } catch (e: IllegalPositionException) {
                return mouse.position
            }
            return mouse.position
        }

        private fun getDirectionTowardsPosition(currentPosition: Position, targetPosition: Position): Direction? {
            val optimalDirections = mutableListOf<Direction>()
            if (currentPosition.x < targetPosition.x) {
                optimalDirections.add(Direction.RIGHT)
            }
            if (currentPosition.x > targetPosition.x) {
                optimalDirections.add(Direction.LEFT)
            }
            if (currentPosition.y < targetPosition.y) {
                optimalDirections.add(Direction.DOWN)
            }
            if (currentPosition.y > targetPosition.y) {
                optimalDirections.add(Direction.UP)
            }
            if (optimalDirections.isEmpty()) {
                return null
            }
            return optimalDirections.random()
        }

    }


}
