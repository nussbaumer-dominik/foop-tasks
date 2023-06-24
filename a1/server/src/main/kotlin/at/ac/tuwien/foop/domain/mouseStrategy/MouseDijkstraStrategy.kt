package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.domain.*

class MouseDijkstraStrategy : MouseStrategy() {
    companion object {
        private fun getEdges(gameBoard: GameBoard, position: Position): MutableSet<Way> {
            val edges = gameBoard.getAllWays(position).map { it.copy() }.toMutableSet()
            /*val edgesToRemove = mutableSetOf<Way>()
            edges.forEach { if (it.targetPosition == position) edgesToRemove.add(it) }
            edges.removeAll(edgesToRemove)*/
            return edges
        }

        //TODO: update positions to entities (with size/hitbox etc.)
        fun findShortestPath(gameBoard: GameBoard, startingPosition: Position, targetPosition: Position): ShortestPathResult {
            val dist = mutableMapOf<Position, Int>()
            val prev = mutableMapOf<Position, Position?>()
            val prevWay = mutableMapOf<Position, Way?>()
            // q ... all nodes of the graph
            val undergroundExitPositions = gameBoard.subways.flatMap { s -> s.exits.map { e -> e.position.copy() } }.toMutableSet()
            val surfaceExitPositions = gameBoard.subways.flatMap { s -> s.exits.map { e -> e.position.copy(subwayId = null) } }.toMutableSet()
            val q = mutableListOf<Position>()
            q.addAll(undergroundExitPositions)
            q.addAll(surfaceExitPositions)
            val edges = getEdges(gameBoard, startingPosition)

            q.forEach { v ->
                dist[v] = Integer.MAX_VALUE
                prev[v] = null
                prevWay[v] = null
            }
            dist[startingPosition] = 0

            while (q.isNotEmpty()) {
                val u = q.minByOrNull { dist[it] ?: 0 }
                q.remove(u)

                if (u == targetPosition) {
                    break // Found the shortest path to target
                }
                edges
                    .filter { it.startingPosition == u }
                    .forEach { edge ->
                        val v = edge.targetPosition
                        val alt = (dist[u] ?: 0) + edge.distance
                        if (alt < (dist[v] ?: 0)) {
                            dist[v] = alt
                            prev[v] = u
                            prevWay[v] = edge
                        }
                    }
            }

            return ShortestPathResult(prev, prevWay, dist, startingPosition, targetPosition)
        }
    }

    fun moveLikeD(mouse: Mouse, gameBoard: GameBoard): Move {
        // choose a target exit from the winning subway as a goal for the mouse
        if (mouse.targetSubwayExitEntity == null) {
            mouse.targetSubwayExitEntity = gameBoard.winningSubway!!.exits.random()
        }
        // if mouse is already at the winning position -> don't move
        //TODO: check if this is working
        if (mouse.intersects(mouse.targetSubwayExitEntity!!)) return Move(null, MouseActions.WAIT, mouse.position)

        val result = findShortestPath(gameBoard, mouse.position, mouse.targetSubwayExitEntity!!.position)
        println("Dijkstra result: $result")
        val path = result.shortestPath(mouse.position, mouse.targetSubwayExitEntity!!.position)
        if (path.isNotEmpty()) {
            //println("Path positions: ${path.map { it.first }}")
            println("Path positions: $path")
            //println("Path ways: ${path.map { it.second }}")
            //TODO: fix this
            //return moveTowardsEntity(mouse, path.first(), gameBoard)
            return Move(null, null, mouse.position)
        }
        return Move(null, MouseActions.WAIT, mouse.position)

    }
    override fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position {
        TODO("Not yet implemented")
    }
}
