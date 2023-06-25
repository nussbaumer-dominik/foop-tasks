package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.models.exceptions.NoMovePossibleException
import at.ac.tuwien.foop.domain.GameBoard
import at.ac.tuwien.foop.domain.Mouse
import at.ac.tuwien.foop.domain.Position

class MouseDirectStrategy : MouseStrategy() {
    override fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position {
        //val exitPositions = gameBoard.getExitPositions()
        val closestExit = gameBoard.winningSubway!!.exits.minByOrNull { mouse.position.distanceTo(it.position) }
        //if mouse is already in winning subway -> do nothing
        if (closestExit!!.position == mouse.position) {
            mouse.subway = gameBoard.subways.first { it.id == closestExit.subwayId }
            return mouse.position
        }
        //TODO: if mouse is trapped in subway -> move to closest exit of the subway and leave the subway
        mouse.subway = null
        return try {
            moveTowardsPosition(mouse.position, closestExit.position, gameBoard)
        } catch (e: NoMovePossibleException) {
            mouse.position
        }
    }
}