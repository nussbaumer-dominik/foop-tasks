package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.models.exceptions.NoMovePossibleException
import at.ac.tuwien.foop.domain.GameBoard
import at.ac.tuwien.foop.domain.Mouse
import at.ac.tuwien.foop.domain.Position

class MouseDirectStrategy : MouseStrategy() {
    override fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position {
        val closestWinningExit = gameBoard.winningSubway!!.exits.minByOrNull { mouse.position.distanceTo(it.position) }
        if (mouse.subway != null) {
            //mouse is in a subway
            if (mouse.subway == gameBoard.winningSubway) {
                //mouse is at the winning subway -> stay
                return mouse.position
            }
            val mouseSubwayExit = mouse.subway!!.exits.find { e -> mouse.intersects(e) }
            if (mouseSubwayExit != null) {
                //mouse intersects currently with an exit -> leave the subway
                mouse.subway = null
                return mouseSubwayExit.position
            } else {
                //in subway, but not at an exit location -> move to the closest exit
                val closestSubwayExit = mouse.subway!!.exits.minByOrNull { e -> e.position.distanceTo(mouse.position) }
                moveTowardsEntity(mouse, closestSubwayExit!!, gameBoard)
            }
        }
        //mouse is not in a subway -> move directly to the closest winning exit
        try {
            if (mouse.intersects(closestWinningExit!!)) {
                //mouse is at the winning exit -> enter the subway
                mouse.subway = gameBoard.winningSubway
                return closestWinningExit.position
            }
            return moveTowardsEntity(mouse, closestWinningExit, gameBoard)
        } catch (e: NoMovePossibleException) {
            return mouse.position
        }
    }
}
