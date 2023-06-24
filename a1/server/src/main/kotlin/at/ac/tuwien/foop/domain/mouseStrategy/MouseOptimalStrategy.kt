package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.domain.*

class MouseOptimalStrategy : MouseStrategy() {
    /**
     * Determines a new position for the mouse on the game board based on an optimal strategy.
     * The strategy takes into account the distance to the closest cat, the current subway the mouse is in,
     * and possible intersection with the cat on its way to a subway. The mouse also randomly chooses an
     * action from MouseActions if it is already in a subway.
     *
     * The function also updates the mouse's subway and target position based on its movement and the
     * state of the game board.
     *
     * @param mouse The Mouse object that represents the current state of the mouse.
     * @param gameBoard The GameBoard object that represents the current state of the game.
     *
     * @return A Position object representing the new position of the mouse.
     */
    override fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position {
        val mouseExit = getExitAtPosition(mouse.position, gameBoard)
        val positionOfClosestCat = mouse.catsPositions.minByOrNull { cp -> cp.distanceTo(mouse.position) }
        val distanceToClosestCat = positionOfClosestCat?.distanceTo(mouse.position) ?: Int.MAX_VALUE

        // Get subway exit pairs
        val subwayExits = gameBoard.getSubwayExitPairs()

        val mouseDirectStrategy = MouseDirectStrategy()

        if (mouse.subway == null) {
            //mouse is not in subway
            mouse.catsPositions = gameBoard.players.map { it.position }
            //TODO: replace value with environment variable
            if (distanceToClosestCat < 3) {
                //quickly enter the next subway
                if (mouseExit != null) {
                    mouse.subway = mouseExit.first
                    return mouse.position
                }
                //move to the closest subway but away from cat
                for (subwayExitPair in subwayExits
                    .sortedBy { (_, e) -> e.position.distanceTo(mouse.position) }) {
                    if (positionOfClosestCat != null && intersect(
                            Pair(mouse.position, subwayExitPair.second.position),
                            Pair(mouse.position, positionOfClosestCat)
                        )
                    ) {
                        //if the cat is on the way to the closest subway -> search for another subway
                        continue
                    }
                    //found an exit that doesn't intersect with the closest cat
                    //set the exit as new target and move to that exit
                    mouse.targetEntity = subwayExitPair.second
                    moveTowardsEntity(mouse, subwayExitPair.second, gameBoard)
                }
                return mouseDirectStrategy.newPosition(mouse, gameBoard)
            } else {
                return if (mouse.targetEntity != null) {
                    if (mouse.intersects(mouse.targetEntity!!)) {
                        mouse.targetEntity = null
                        val exit = subwayExits
                            .firstOrNull { (_, e) -> e.position == mouse.position }?.second
                        if (exit != null) {
                            mouse.subway = gameBoard.subways.first { it.id == exit.subwayId }
                        }
                    }
                    moveTowardsEntity(mouse, mouse.targetEntity!!, gameBoard)
                } else
                    mouseDirectStrategy.newPosition(mouse, gameBoard)
            }
        } else {
            //mouse is in subway
            if (mouseExit != null) {
                val possibleActions = MouseActions.values()
                return when (possibleActions.random()) {
                    MouseActions.WAIT -> {
                        mouse.position
                    }

                    MouseActions.MOVE -> {
                        mouseDirectStrategy.newPosition(mouse, gameBoard)
                    }

                    MouseActions.LEAVE_SUBWAY -> {
                        mouse.subway = null
                        //TODO: update location of cats
                        mouse.position
                    }
                }
            } else {
                //mouse is in subway but not at exit
                val closestExitToWinningSubway = subwayExits
                    .filter { (s, _) -> s == mouse.subway }
                    .minByOrNull { (_, e) -> e.position.distanceTo(mouse.position) }
                return moveTowardsEntity(
                    mouse,
                    closestExitToWinningSubway!!.second,
                    gameBoard
                )
            }
        }
    }

    /**
     * Retrieves the subway exit at a specified position on the game board.
     * This function returns the first Subway-Exit pair for which the Exit's position
     * matches the input position.
     *
     * @param position The Position object that defines the coordinates on the game board to check.
     * @param gameBoard The GameBoard object that represents the game's current state.
     *
     * @return A Pair of Subway and Exit objects that represent a subway's exit at the specified position.
     * If no exit is found at the specified position, returns null.
     */
    private fun getExitAtPosition(position: Position, gameBoard: GameBoard): Pair<Subway, Exit>? {
        val subwayExits = gameBoard.getSubwayExitPairs()
        return subwayExits.firstOrNull { (_, e) -> e.position == position }
    }

    /**
     * Determines whether two line segments intersect.
     *
     * @param line1 The first line segment defined by a pair of positions.
     * @param line2 The second line segment defined by a pair of positions.
     * @return true if the line segments intersect, false otherwise.
     */
    private fun intersect(line1: Pair<Position, Position>, line2: Pair<Position, Position>): Boolean {
        val x1 = line1.first.x
        val y1 = line1.first.y
        val x2 = line1.second.x
        val y2 = line1.second.y
        val x3 = line2.first.x
        val y3 = line2.first.y
        val x4 = line2.second.x
        val y4 = line2.second.y

        val a = (y4 - y3) * (x2 - x1) - (y2 - y1) * (x4 - x3)
        val b = (y1 - y3) * (x2 - x1) - (y2 - y1) * (x1 - x3)
        val d = (y4 - y3) * (x4 - x1) - (y4 - y1) * (x4 - x3)

        if (a == 0) {
            return false
        }

        val ua = b / a.toDouble()
        val ub = d / a.toDouble()

        return ua in 0.0..1.0 && ub in 0.0..1.0
    }
}
