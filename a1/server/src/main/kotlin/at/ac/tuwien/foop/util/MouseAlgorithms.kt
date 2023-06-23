package at.ac.tuwien.foop.util

import at.ac.tuwien.foop.common.domain.Direction
import at.ac.tuwien.foop.common.exceptions.IllegalPositionException
import at.ac.tuwien.foop.common.exceptions.NoMovePossibleException
import at.ac.tuwien.foop.domain.*

class MouseAlgorithms {
    companion object {
        fun moveRandom(mouse: Mouse, gameBoard: GameBoard): Position {
            while (true) {
                val direction = getRandomDirection()
                val newPosition = mouse.position.getNewPosition(direction)
                try {
                    val field = gameBoard.getFieldAtPosition(newPosition)
                    if (field is Exit) {
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
            val closestExit =
                gameBoard.winningSubway!!.exits.minByOrNull { mouse.position.distanceTo(it.position) }
            //if mouse is already in winning subway -> do nothing
            if (closestExit!!.position == mouse.position) {
                mouse.subway = gameBoard.subways.first { it.id == closestExit.subwayId }
                return mouse.position
            }
            //TODO: if mouse is trapped in subway -> move to closest exit of the subway and leave the subway
            mouse.subway = null
            return try {
                moveTowardsPosition(mouse.position, closestExit.position, gameBoard)

            } catch (NoMovePossibleException: NoMovePossibleException) {
                mouse.position
            }
        }

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
                throw NoMovePossibleException("Position: $currentPosition")
            }
            return optimalDirections
        }

        private fun moveTowardsPosition(
            currentPosition: Position,
            targetPosition: Position,
            gameBoard: GameBoard
        ): Position {
            val directions = getDirectionsTowardsPosition(currentPosition, targetPosition) ?: return currentPosition
            //If no optimal move is possible, move random
            //var suboptimalDirections = Direction.values().filter { d -> directions.none { it == d } }
            val otherDirections = Direction.values().toMutableList()
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

            Direction.values().random()

            throw NoMovePossibleException("Position: $currentPosition")
        }

        fun moveOptimal(mouse: Mouse, gameBoard: GameBoard): Position {
            val mouseExit = getExitAtPosition(mouse.position, gameBoard)
            val positionOfClosestCat = mouse.catsPositions.minByOrNull { cp -> cp.distanceTo(mouse.position) }
            val distanceToClosestCat = positionOfClosestCat?.distanceTo(mouse.position) ?: Int.MAX_VALUE
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
                    for (subwayExitPair in gameBoard.getSubwayExitPairs()
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
                        mouse.targetPosition = subwayExitPair.second.position
                        moveTowardsPosition(mouse.position, subwayExitPair.second.position, gameBoard)
                    }
                    return moveDirectlyTowardsClosestWinningExit(mouse, gameBoard)
                } else {
                    return if (mouse.targetPosition != null) {
                        if (mouse.position == mouse.targetPosition) {
                            mouse.targetPosition = null
                            val exit = gameBoard.getSubwayExitPairs()
                                .firstOrNull { (_, e) -> e.position == mouse.position }?.second
                            if (exit != null) {
                                mouse.subway = gameBoard.subways.first { it.id == exit.subwayId }
                            }
                        }
                        moveTowardsPosition(mouse.position, mouse.targetPosition!!, gameBoard)
                    } else
                        moveDirectlyTowardsClosestWinningExit(mouse, gameBoard)
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
                            moveDirectlyTowardsClosestWinningExit(mouse, gameBoard)
                        }

                        MouseActions.LEAVE_SUBWAY -> {
                            mouse.subway = null
                            //TODO: update location of cats
                            mouse.position
                        }
                    }
                } else {
                    //mouse is in subway but not at exit
                    val closestExitToWinningSubway = gameBoard.getSubwayExitPairs()
                        .filter { (s, _) -> s == mouse.subway }
                        .minByOrNull { (_, e) -> e.position.distanceTo(mouse.position) }
                    return moveTowardsPosition(
                        mouse.position,
                        closestExitToWinningSubway!!.second.position,
                        gameBoard
                    )
                }
            }
        }

        private fun getExitAtPosition(position: Position, gameBoard: GameBoard): Pair<Subway, Exit>? {
            val subwayExits = gameBoard.getSubwayExitPairs()
            return subwayExits.firstOrNull { (_, e) -> e.position == position }
        }

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


}
