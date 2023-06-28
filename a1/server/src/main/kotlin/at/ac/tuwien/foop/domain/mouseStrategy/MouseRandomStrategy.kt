package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.common.models.exceptions.NoMovePossibleException
import at.ac.tuwien.foop.domain.*

class MouseRandomStrategy : MouseStrategy() {
    private var chosenSubway: Subway? = null
    private var chosenSubwayExit: Exit? = null
    private var previousExit: Exit? = null

    // TODO: add subwayId into the positions
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
                if (previousExit != null && mouseSubwayExit.intersects(previousExit!!)) {
                    //mouse is at the same exit as before -> move to a different exit of the same subway
                    return moveToExit(mouse, gameBoard, chosenSubwayExit!!)
                }

                mouse.subway = null
                chosenSubway = null
                chosenSubwayExit = null
                return mouseSubwayExit.position
            } else {
                //in subway, but not at an exit location -> move to the closest exit
                val closestSubwayExit = mouse.subway!!.exits.minByOrNull { e -> e.position.distanceTo(mouse.position) }
                val remainingExits = mouse.subway!!.exits.filter { e -> e != closestSubwayExit }
                moveToExit(mouse, gameBoard, remainingExits.random())
            }
        }
        //mouse is not in a subway -> move directly to the closest winning exit
        if (mouse.intersects(chosenSubwayExit!!)) {
            //mouse is at the chosen exit -> enter the subway
            val remainingExits = chosenSubway!!.exits.filter { e -> e != chosenSubwayExit }
            mouse.subway = chosenSubway!!.copy()
            previousExit = chosenSubwayExit!!.copy()
            chosenSubwayExit = remainingExits.random()
            println("previousExit.subwayId: ${previousExit!!.subwayId}")
            return previousExit!!.position
        }
        println("chosenSubwayExit.subwayId: ${chosenSubwayExit!!.subwayId}")
        return moveToExit(mouse, gameBoard, chosenSubwayExit!!)
    }

    private fun moveToExit(mouse: Mouse, gameBoard: GameBoard, exit: Exit): Position {
        return try {
            moveTowardsEntity(mouse, exit, gameBoard)
        } catch (e: NoMovePossibleException) {
            mouse.position
        }
    }
}