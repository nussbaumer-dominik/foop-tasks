package at.ac.tuwien.foop.domain

class ShortestPathResult(
    val prev: MutableMap<Entity, Entity?>,
    val prevWay: MutableMap<Entity, Way?>,
    val dist: MutableMap<Entity, Int>,
    val startingEntity: Entity,
    val targetEntity: Entity
) {
    fun shortestPathWithWay(from: Entity = startingEntity, to: Entity = targetEntity, list: List<Pair<Entity, Way>> = emptyList(), alreadyAddedToList: MutableSet<Pair<Entity, Way>> = mutableSetOf()): List<Pair<Entity, Way?>> {
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

    fun shortestPath(from: Entity = startingEntity, to: Entity = targetEntity, list: List<Entity> = emptyList()): List<Entity> {
        val last = prev[to] ?: return if (from.position == to.position) {
            list + to
        } else {
            emptyList()
        }
        return shortestPath(from, last, list) + to
    }

    fun shortestDistance(): Int? {
        val shortest = dist[targetEntity]
        if (shortest == Integer.MAX_VALUE) {
            return null
        }
        return shortest
    }
}
