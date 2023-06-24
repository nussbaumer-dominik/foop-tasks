package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.exceptions.IllegalPositionException
import at.ac.tuwien.foop.common.exceptions.NoMovePossibleException
import at.ac.tuwien.foop.domain.*

abstract class MouseStrategy {
    abstract fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position

    private fun getDirectionsTowardsEntity(
        movingEntity: MovingEntity,
        targetEntity: Entity
    ): MutableSet<Direction>? {
        if (movingEntity.intersects(targetEntity)) {
            return null
        }
        val optimalDirections = mutableSetOf<Direction>()
        if (movingEntity.position.x + movingEntity.moveSize <= targetEntity.position.x) {
            optimalDirections.add(Direction.RIGHT)
        }
        if (movingEntity.position.x - movingEntity.moveSize >= targetEntity.position.x) {
            optimalDirections.add(Direction.LEFT)
        }
        if (movingEntity.position.y + movingEntity.moveSize <= targetEntity.position.y) {
            optimalDirections.add(Direction.DOWN)
        }
        if (movingEntity.position.y - movingEntity.moveSize >= targetEntity.position.y) {
            optimalDirections.add(Direction.UP)
        }
        if (optimalDirections.isEmpty()) {
            throw NoMovePossibleException("MovingEntity: $movingEntity")
        }

        return optimalDirections.shuffled().toMutableSet()
    }

    protected fun moveTowardsEntity(
        movingEntity: MovingEntity,
        targetEntity: Entity,
        gameBoard: GameBoard
    ): Position {
        val directions =
            getDirectionsTowardsEntity(movingEntity, targetEntity) ?: return movingEntity.position

        //If no optimal move is possible, move random
        val allDirections = Direction.values().toMutableList()
        allDirections.shuffle()
        directions.addAll(allDirections)

        for (direction in directions) {
            val newPosition = movingEntity.position.getNewPosition(direction, movingEntity.moveSize)
            var newEntity = ConcreteMovingEntity(movingEntity)
            newEntity = newEntity.copyWith(position = newPosition)
            try {
                if (!gameBoard.players.any { p -> p.intersects(newEntity) }) {
                    return newPosition
                }
            } catch (e: IllegalPositionException) {
                continue
            }
        }

        Direction.values().random()

        throw NoMovePossibleException("Position: $movingEntity")
    }
}
