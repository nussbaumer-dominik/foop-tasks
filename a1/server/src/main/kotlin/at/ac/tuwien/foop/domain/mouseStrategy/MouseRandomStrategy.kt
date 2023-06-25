package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.exceptions.NoMovePossibleException
import at.ac.tuwien.foop.domain.*
import at.ac.tuwien.foop.common.models.dtos.socket.DirectionDto
import at.ac.tuwien.foop.common.models.exceptions.IllegalPositionException
import at.ac.tuwien.foop.domain.Exit
import at.ac.tuwien.foop.domain.GameBoard
import at.ac.tuwien.foop.domain.Mouse
import at.ac.tuwien.foop.domain.Position

class MouseRandomStrategy : MouseStrategy() {
    private var chosenSubway: Subway? = null
    private var chosenSubwayExit: Exit? = null

    override fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position {
        if (chosenSubwayExit == null) {
            chosenSubway = gameBoard.subways.random()
            chosenSubwayExit = chosenSubway!!.exits.random()
        }

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
                chosenSubwayExit = null
                return mouseSubwayExit.position
            } else {
                //in subway, but not at an exit location -> move to the closest exit
                val closestSubwayExit = mouse.subway!!.exits.minByOrNull { e -> e.position.distanceTo(mouse.position) }
                moveTowardsEntity(mouse, closestSubwayExit!!, gameBoard)
            }
        }
        //mouse is not in a subway -> move directly to the closest winning exit
        try {
            if (mouse.intersects(chosenSubwayExit!!)) {
                //mouse is at the chosen exit -> enter the subway
                mouse.subway = chosenSubway!!.copy()
                chosenSubway = null
                return chosenSubwayExit!!.position
            }
            return moveTowardsEntity(mouse, chosenSubwayExit!!, gameBoard)
        } catch (e: NoMovePossibleException) {
            return mouse.position
        }
    }
}