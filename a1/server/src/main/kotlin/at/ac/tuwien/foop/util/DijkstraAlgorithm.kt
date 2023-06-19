package at.ac.tuwien.foop.util

import at.ac.tuwien.foop.common.domain.GameBoard
import at.ac.tuwien.foop.common.domain.Position
import at.ac.tuwien.foop.common.domain.Way

class DijkstraAlgorithm {
    companion object {
        private fun getEdges(gameBoard: GameBoard, position: Position): MutableSet<Way> {
            val edges = gameBoard.getAllWays(position).map { it.copy() }.toMutableSet()
            /*val edgesToRemove = mutableSetOf<Way>()
            edges.forEach { if (it.targetPosition == position) edgesToRemove.add(it) }
            edges.removeAll(edgesToRemove)*/
            return edges
        }

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
                    break // Found shortest path to target
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


}

data class ShortestPathResult(
    val prev: MutableMap<Position, Position?>,
    val prevWay: MutableMap<Position, Way?>,
    val dist: MutableMap<Position, Int>,
    val startingPosition: Position,
    val targetPosition: Position
) {
    fun shortestPathWithWay(from: Position = startingPosition, to: Position = targetPosition, list: List<Pair<Position, Way>> = emptyList(), alreadyAddedToList: MutableSet<Pair<Position, Way>> = mutableSetOf()): List<Pair<Position, Way?>> {
        if (from == to) {
            return list
        }
        val last = Pair(prev[to], prevWay[prev[to]])
        if (alreadyAddedToList.contains(last)) return emptyList()
        println("Last: $last")
        if (last.first != null) {
            if (list.contains(last)) {
                return emptyList()
            }
            alreadyAddedToList.add(Pair(to, prevWay[to]!!))
            var prevWay = prevWay[to]
            //if (prevWay?.targetPosition!!.equalCoordinates(from)) prevWay = null
            return shortestPathWithWay(from, last.first!!, list, alreadyAddedToList) + Pair(to, prevWay)
        }
        return emptyList()

    }

    fun shortestPath(from: Position = startingPosition, to: Position = targetPosition, list: List<Position> = emptyList()): List<Position> {
        val last = prev[to] ?: return if (from == to) {
            list + to
        } else {
            emptyList()
        }
        return shortestPath(from, last, list) + to
    }

    fun shortestDistance(): Int? {
        val shortest = dist[targetPosition]
        if (shortest == Integer.MAX_VALUE) {
            return null
        }
        return shortest
    }
}
