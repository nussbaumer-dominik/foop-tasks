package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.models.dtos.socket.DirectionDto
import at.ac.tuwien.foop.common.models.exceptions.IllegalPositionException
import at.ac.tuwien.foop.common.models.exceptions.NoMovePossibleException
import at.ac.tuwien.foop.domain.GameBoard
import at.ac.tuwien.foop.domain.Mouse
import at.ac.tuwien.foop.domain.Player
import at.ac.tuwien.foop.domain.Position

abstract class MouseStrategy {
    abstract fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position

    private fun getDirectionsTowardsPosition(
        currentPosition: Position,
        targetPosition: Position
    ): MutableSet<DirectionDto>? {
        if (currentPosition == targetPosition) {
            return null
        }
        val optimalDirections = mutableSetOf<DirectionDto>()
        if (currentPosition.x < targetPosition.x) {
            optimalDirections.add(DirectionDto.RIGHT)
        }
        if (currentPosition.x > targetPosition.x) {
            optimalDirections.add(DirectionDto.LEFT)
        }
        if (currentPosition.y < targetPosition.y) {
            optimalDirections.add(DirectionDto.DOWN)
        }
        if (currentPosition.y > targetPosition.y) {
            optimalDirections.add(DirectionDto.UP)
        }
        if (optimalDirections.isEmpty()) {
            //throw NoMovePossibleException("Position: $currentPosition")
        }
        return optimalDirections
    }

    protected fun moveTowardsPosition(
        currentPosition: Position,
        targetPosition: Position,
        gameBoard: GameBoard
    ): Position {
        val directions = getDirectionsTowardsPosition(currentPosition, targetPosition) ?: return currentPosition
        //If no optimal move is possible, move random
        //var suboptimalDirections = Direction.values().filter { d -> directions.none { it == d } }
        val otherDirections = DirectionDto.values().toMutableList()
        otherDirections.shuffle()
        //println("Shuffled directions: $otherDirections")
        directions.addAll(otherDirections)
        for (direction in directions) {
            val newPosition = currentPosition.getNewPosition(direction)
            try {
                val field = gameBoard.getFieldAtPosition(newPosition)
                if (field !is Player) {
                    return newPosition
                }
            } catch (e: IllegalPositionException) {
                continue
            }
        }

        DirectionDto.values().random()

        throw NoMovePossibleException("Position: $currentPosition")
    }
}