package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.exceptions.IllegalPositionException
import at.ac.tuwien.foop.common.exceptions.NoMovePossibleException
import at.ac.tuwien.foop.domain.*

abstract class MouseStrategy {
    abstract fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position

    private fun getDirectionsTowardsPosition(
        currentPosition: Position,
        targetPosition: Position
    ): MutableSet<Direction>? {
        if (currentPosition == targetPosition) {
            return null
        }
        val optimalDirections = mutableSetOf<Direction>()
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
            //throw NoMovePossibleException("Position: $currentPosition")
        }
        return optimalDirections
    }

    protected fun moveTowardsPosition(
        currentEntity: Entity,
        targetPosition: Position,
        gameBoard: GameBoard
    ): Position {
        val directions = getDirectionsTowardsPosition(currentEntity.position, targetPosition) ?: return currentEntity.position
        //If no optimal move is possible, move random
        //var suboptimalDirections = Direction.values().filter { d -> directions.none { it == d } }
        val otherDirections = Direction.values().toMutableList()
        otherDirections.shuffle()
        //println("Shuffled directions: $otherDirections")
        directions.addAll(otherDirections)
        for (direction in directions) {
            val newPosition = currentEntity.position.getNewPosition(direction)
            val newEntity = ConcreteEntity(currentEntity)
            newEntity.copyWith(position = newPosition)
            try {
                if (!gameBoard.players.any {p -> p.intersects(newEntity)}) {
                    return newPosition
                }
            } catch (e: IllegalPositionException) {
                continue
            }
        }

        Direction.values().random()

        throw NoMovePossibleException("Position: $currentEntity")
    }
}
