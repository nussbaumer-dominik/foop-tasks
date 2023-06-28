package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.models.dtos.socket.DirectionDto
import at.ac.tuwien.foop.common.models.exceptions.IllegalPositionException
import at.ac.tuwien.foop.common.models.exceptions.NoMovePossibleException
import at.ac.tuwien.foop.domain.*

abstract class MouseStrategy {
    abstract fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position

    private fun getDirectionsTowardsEntity(
        movingEntity: MovingEntity,
        targetEntity: Entity
    ): MutableSet<DirectionDto>? {
        if (movingEntity.intersects(targetEntity)) {
            return null
        }
        val optimalDirections = mutableSetOf<DirectionDto>()
        if (movingEntity.position.x + movingEntity.moveSize <= targetEntity.position.x) {
            optimalDirections.add(DirectionDto.RIGHT)
        }
        if (movingEntity.position.x - movingEntity.moveSize >= targetEntity.position.x) {
            optimalDirections.add(DirectionDto.LEFT)
        }
        if (movingEntity.position.y + movingEntity.moveSize <= targetEntity.position.y) {
            optimalDirections.add(DirectionDto.DOWN)
        }
        if (movingEntity.position.y - movingEntity.moveSize >= targetEntity.position.y) {
            optimalDirections.add(DirectionDto.UP)
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
        val optimalDirections = getDirectionsTowardsEntity(movingEntity, targetEntity)
            ?: return movingEntity.position

        val newPosition = if (optimalDirections.size == 2) {
            //if there are two optimal directions -> move diagonal
            val firstMovePosition =
                movingEntity.position.getNewPosition(optimalDirections.elementAt(0), movingEntity.moveSize / 2)
            firstMovePosition.getNewPosition(optimalDirections.elementAt(1), movingEntity.moveSize / 2)
        } else {
            movingEntity.position.getNewPosition(optimalDirections.first(), movingEntity.moveSize)
        }

        var newEntity = ConcreteMovingEntity(movingEntity)
        newEntity = newEntity.copyWith(position = newPosition)
        try {
            if (gameBoard.players.none { p -> p.intersects(newEntity) } || movingEntity.position.subwayId != null) {
                return newPosition
            }
        } catch (e: IllegalPositionException) {
            return movingEntity.position
        }

        DirectionDto.values().random()

        throw NoMovePossibleException("Position: $movingEntity")
    }
}
