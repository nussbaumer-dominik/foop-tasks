package at.ac.tuwien.foop.domain

class ShortestPathResult(
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
            val prevWay = prevWay[to]
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
