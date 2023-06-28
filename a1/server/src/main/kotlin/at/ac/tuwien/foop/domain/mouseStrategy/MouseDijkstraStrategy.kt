package at.ac.tuwien.foop.domain.mouseStrategy

import at.ac.tuwien.foop.domain.*

class MouseDijkstraStrategy : MouseStrategy() {
    companion object {
        private fun getEdges(gameBoard: GameBoard, entity: Entity): MutableSet<Way> {
            val edges = gameBoard.getAllWays(entity).map { it.copy() }.toMutableSet()
            /*val edgesToRemove = mutableSetOf<Way>()
            edges.forEach { if (it.targetPosition == position) edgesToRemove.add(it) }
            edges.removeAll(edgesToRemove)*/
            return edges
        }

        //TODO: update positions to entities (with size/hitbox etc.)
        fun findShortestPath(gameBoard: GameBoard, startingEntity: Entity, targetEntity: Entity): ShortestPathResult {
            val dist = mutableMapOf<Entity, Int>()
            val prev = mutableMapOf<Entity, Entity?>()
            val prevWay = mutableMapOf<Entity, Way?>()
            val undergroundExitEntities = gameBoard.subways.flatMap { s -> s.exits.map { e -> ConcreteEntity(e) } }.toMutableSet()
            val surfaceExitEntities = gameBoard.subways.flatMap { s -> s.exits.map { e -> ConcreteEntity(e).copyWith(position = e.position.copy(subwayId = null)) } }.toMutableSet()
            // q ... all nodes of the graph
            val q = mutableListOf<Entity>()
            q.addAll(undergroundExitEntities)
            q.addAll(surfaceExitEntities)
            val edges = getEdges(gameBoard, startingEntity)

            q.forEach { v ->
                dist[v] = Integer.MAX_VALUE
                prev[v] = null
                prevWay[v] = null
            }
            dist[startingEntity] = 0

            while (q.isNotEmpty()) {
                val u = q.minByOrNull { dist[it] ?: 0 }
                q.remove(u)

                if (u?.position == targetEntity.position) {
                    break // Found the shortest path to target
                }
                edges
                    .filter { it.startingEntity.position == u?.position }
                    .forEach { edge ->
                        val v = edge.targetEntity
                        val alt = (dist[u] ?: 0) + edge.distance
                        if (alt < (dist[v] ?: 0)) {
                            dist[v] = alt
                            prev[v] = u
                            prevWay[v] = edge
                        }
                    }
            }

            return ShortestPathResult(prev, prevWay, dist, startingEntity, targetEntity)
        }
    }

    fun moveLikeJagger(mouse: Mouse, gameBoard: GameBoard): Move {
        // choose a target exit from the winning subway as a goal for the mouse
        if (mouse.targetSubwayExit == null) {
            mouse.targetSubwayExit = gameBoard.winningSubway!!.exits.random()
        }
        // if mouse is already at the winning position -> don't move
        //TODO: check if this is working
        if (mouse.intersects(mouse.targetSubwayExit!!)) {
            var newPosition = mouse.position
            if (mouse.position.subwayId == null) {
                //enter subway
                newPosition = mouse.position.copy(subwayId = mouse.targetSubwayExit!!.subwayId)
            }
            return Move(null, MouseActions.WAIT, newPosition)
        }

        val result = findShortestPath(gameBoard, mouse, mouse.targetSubwayExit!!)
        //println("Dijkstra result: $result")
        val path = result.shortestPath(mouse, mouse.targetSubwayExit!!)
        println("Path: $path")
        if (path.isNotEmpty()) {
            //println("Path positions: ${path.map { it.first }}")
            println("Path positions: $path")
            //println("Path ways: ${path.map { it.second }}")
            //TODO: fix this
            val newPos = moveTowardsEntity(mouse, path.first(), gameBoard)
            return Move(null, null, mouse.position)
        }
        return Move(null, MouseActions.WAIT, mouse.position)

    }
    override fun newPosition(mouse: Mouse, gameBoard: GameBoard): Position {
        val move = moveLikeJagger(mouse, gameBoard)
        if ((mouse.position.subwayId != null || mouse.subway != null) && move.newPosition.subwayId == null) {
            //leave subway
            mouse.subway = null
        } else if ((mouse.position.subwayId == null || mouse.subway == null) && move.newPosition.subwayId != null) {
            mouse.subway = gameBoard.subways.find { s -> s.id == move.newPosition.subwayId }
        }
        return move.newPosition
    }
}
