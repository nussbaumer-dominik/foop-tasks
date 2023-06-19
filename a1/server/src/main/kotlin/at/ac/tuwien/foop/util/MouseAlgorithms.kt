package at.ac.tuwien.foop.util

import at.ac.tuwien.foop.common.domain.*
import at.ac.tuwien.foop.common.exceptions.IllegalPositionException
import at.ac.tuwien.foop.common.exceptions.NoMovePossibleException

class MouseAlgorithms {
    companion object {
        fun moveRandom(mouse: Mouse, gameBoard: GameBoard): Position {
            while (true) {
                val direction = getRandomDirection()
                val newPosition = mouse.position.getNewPosition(direction)
                try {
                    val field = gameBoard.getFieldAtPosition(newPosition)
                    if (field is EmptyField || field is Exit) {
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

        fun moveDirectlyTowardsClosestWinningExit(mouse: Mouse, gameBoard: GameBoard): Move {
            //val exitPositions = gameBoard.getExitPositions()
            val closestExit =
                gameBoard.winningSubway!!.exits.minByOrNull { mouse.position.distanceTo(it.position) }
            //if mouse is already in winning subway -> do nothing
            if (closestExit!!.position == mouse.position) {
                //mouse.subway = gameBoard.subways.first { it.id == closestExit.subwayId }
                return Move(Direction.STAY, mouse.position)
            }
            //TODO: if mouse is trapped in subway -> move to closest exit of the subway and leave the subway
            //mouse.subway = null
            return try {
                moveTowardsPosition(mouse.position, closestExit.position, gameBoard)

            } catch (NoMovePossibleException: NoMovePossibleException) {
                Move(Direction.STAY, mouse.position)
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
            if (currentPosition.x == targetPosition.x && currentPosition.y == targetPosition.y) {
                optimalDirections.add(Direction.ACCESS_SUBWAY)
            }
            if (optimalDirections.isEmpty()) {
                throw NoMovePossibleException("Position: $currentPosition")
            }
            return optimalDirections.shuffled().toMutableSet()
        }

        private fun moveTowardsPosition(
            currentPosition: Position,
            targetPosition: Position,
            gameBoard: GameBoard
        ): Move {
            val directions = getDirectionsTowardsPosition(currentPosition, targetPosition) ?: return Move(Direction.STAY, currentPosition)
            //If no optimal move is possible, move random
            //var suboptimalDirections = Direction.values().filter { d -> directions.none { it == d } }
            val otherDirections = Direction.values().toMutableList()
            otherDirections.shuffle()
            println("Shuffled directions: $otherDirections")
            directions.addAll(otherDirections)
            for (direction in directions) {
                var accessSubway: Subway? = null
                if (direction == Direction.ACCESS_SUBWAY) {
                        accessSubway = gameBoard.getSubwayExitPairs().firstOrNull {(s, e) -> e.position.equalCoordinates(currentPosition)}?.first
                }
                val newPosition = currentPosition.getNewPosition(direction, accessSubway?.id)
                try {
                    val field = gameBoard.getFieldAtPosition(newPosition)
                    if (field !is Player) {
                        return Move(direction, newPosition)
                    }
                } catch (e: IllegalPositionException) {
                    continue
                }
            }

            throw NoMovePossibleException("Position: $currentPosition")
        }

        /*fun moveOptimal(mouse: Mouse, gameBoard: GameBoard): Position {
            val mouseExit = getExitAtPosition(mouse.position, gameBoard)
            val positionOfClosestCat = mouse.catsPositions.minByOrNull { cp -> cp.distanceTo(mouse.position) }
            val distanceToClosestCat = positionOfClosestCat?.distanceTo(mouse.position) ?: Int.MAX_VALUE
            if (mouse.subway == null) {
                //mouse is not in subway
                mouse.catsPositions = gameBoard.cats.map { it.position }
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
                        mouse.targetSubwayExitPosition = subwayExitPair.second.position
                        moveTowardsPosition(mouse.position, subwayExitPair.second.position, gameBoard)
                    }
                    return moveDirectlyTowardsClosestWinningExit(mouse, gameBoard)
                } else {
                    return if (mouse.targetSubwayExitPosition != null) {
                        if (mouse.position == mouse.targetSubwayExitPosition) {
                            mouse.targetSubwayExitPosition = null
                            val exit = gameBoard.getSubwayExitPairs()
                                .firstOrNull() { (_, e) -> e.position == mouse.position }?.second
                            if (exit != null) {
                                mouse.subway = gameBoard.subways.first { it.id == exit.subwayId }
                            }
                        }
                        moveTowardsPosition(mouse.position, mouse.targetSubwayExitPosition!!, gameBoard)
                    } else
                        moveDirectlyTowardsClosestWinningExit(mouse, gameBoard)
                }
            } else {
                //mouse is in subway
                if (mouseExit != null) {
                    val possibleActions = Actions.values()
                    return when (possibleActions.random()) {
                        Actions.WAIT -> {
                            mouse.position
                        }

                        Actions.MOVE -> {
                            moveDirectlyTowardsClosestWinningExit(mouse, gameBoard)
                        }

                        Actions.LEAVE_SUBWAY -> {
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
                    return moveTowardsPosition(mouse.position, closestExitToWinningSubway!!.second.position, gameBoard)
                }
            }
        }

         */

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

        fun moveLikeJagger(mouse: Mouse, gameBoard: GameBoard): Move {
            if (mouse.nextTargetPosition != null) {
                val move = moveTowardsPosition(mouse.position, mouse.nextTargetPosition!!, gameBoard)
                if (move.newPosition == mouse.nextTargetPosition) mouse.nextTargetPosition = null
                return move
            }
            val (distance, ancestor) = dijkstra(gameBoard, mouse.position)
            println("Ancestor: $ancestor")
            val wayPositions = mutableListOf<Position>()
            val targetSubwayExitPositions = gameBoard.winningSubway!!.exits.map { it.position }
            var targetSubwayExitPosition = mouse.targetSubwayExitPosition
            if (targetSubwayExitPosition == null) {
                targetSubwayExitPosition = targetSubwayExitPositions.minBy { p -> p.distanceTo(mouse.position) }
                mouse.targetSubwayExitPosition = targetSubwayExitPosition
            }
            wayPositions.add(targetSubwayExitPosition)
            while (ancestor[targetSubwayExitPosition] != null) {
                targetSubwayExitPosition = ancestor[targetSubwayExitPosition]!!
                wayPositions.add(0, targetSubwayExitPosition)
            }
            println("Mouse position: ${mouse.position}")
            println("The Way: $wayPositions")
            var nextTargetPosition = wayPositions.first()
            if (mouse.position == nextTargetPosition) {
                if (gameBoard.getFieldAtPosition(mouse.position) is Exit) {
                    if (mouse.position.subwayId == null) {
                        mouse.position.subwayId =
                            gameBoard.getSubwayExitPairs().first { (s, e) -> e.position == mouse.position }.first.id
                    } else {
                        mouse.position.subwayId = null
                    }
                }
                if (wayPositions.size > 1)
                    nextTargetPosition = wayPositions[1]
            }
            println("Next target position: $nextTargetPosition")
            mouse.nextTargetPosition = nextTargetPosition
            return moveTowardsPosition(mouse.position, nextTargetPosition, gameBoard)
        }

        private fun dijkstra(
            gameBoard: GameBoard,
            startingPosition: Position,
        ): Pair<Map<Position, Int>, Map<Position, Position?>> {
            val (distance, ancestor, remainingPositions) = initDijkstra(gameBoard, startingPosition)
            while (remainingPositions.size > 0) {
                val u = remainingPositions.minBy { p -> distance[p]!! }
                remainingPositions.remove(u)
                for (way in gameBoard.getPossibleWays(u)) {
                    if (remainingPositions.contains(way.targetPosition)) {
                        val (d, a) = distanceUpdate(u, way.targetPosition, distance, ancestor, gameBoard)
                        distance[d.first] = d.second
                        ancestor[a.first] = a.second
                    }
                }
            }
            println("Distance: $distance")
            return Pair(distance, ancestor)
        }

        private fun distanceUpdate(
            u: Position,
            v: Position,
            distance: MutableMap<Position, Int>,
            ancestor: MutableMap<Position, Position?>,
            gameBoard: GameBoard
        ): Pair<Pair<Position, Int>, Pair<Position, Position>> {
            //TODO: take the cat into account ->
            //check if the two locations are connected via a subway
            var distanceBetweenPoints = u.distanceTo(v)
            if (!gameBoard.arePositionsConnectedViaSubway(u, v)) {
                distanceBetweenPoints *= 2
            }
            val alternativeDistance = distance[u]!!.plus(distanceBetweenPoints)
            if (alternativeDistance < distance[v]!!) {
                distance[v] = alternativeDistance
                ancestor[v] = u
            }
            return Pair(Pair(v, alternativeDistance), Pair(v, u))
        }

        private fun initDijkstra(
            gameBoard: GameBoard,
            startingPosition: Position
        ): Triple<MutableMap<Position, Int>, MutableMap<Position, Position?>, MutableSet<Position>> {
            val distance = mutableMapOf<Position, Int>()
            val ancestor = mutableMapOf<Position, Position?>()
            val exitPositions = gameBoard.getSubwayExitPairs().map { it.second.position }.toMutableSet()
            exitPositions.add(startingPosition)
            for (exitPosition in exitPositions) {
                distance[exitPosition] = Int.MAX_VALUE
                ancestor[exitPosition] = null
            }
            distance[startingPosition] = 0
            return Triple(distance, ancestor, exitPositions)
        }

        fun moveLikeD(mouse: Mouse, gameBoard: GameBoard): Move {
            // choose a target exit from the winning subway as a goal for the mouse
            if (mouse.targetSubwayExitPosition == null) {
                mouse.targetSubwayExitPosition = gameBoard.winningSubway!!.exits.random().position
            }
            // if mouse is already at the winning position -> don't move
            if (mouse.position == mouse.targetSubwayExitPosition) return Move(Direction.STAY, mouse.position)

            val result = DijkstraAlgorithm.findShortestPath(gameBoard, mouse.position, mouse.targetSubwayExitPosition!!)
            println("Dijkstra result: $result")
            val path = result.shortestPath(mouse.position, mouse.targetSubwayExitPosition!!)
            if (path.isNotEmpty()) {
                //println("Path positions: ${path.map { it.first }}")
                println("Path positions: $path")
                //println("Path ways: ${path.map { it.second }}")
                return moveTowardsPosition(mouse.position, path.first(), gameBoard)
            }
            return Move(Direction.STAY, mouse.position)

        }

    }


}
